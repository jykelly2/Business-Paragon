package sheridan.yamazaki.businessparagon.repository

import android.app.Activity
import android.app.Application
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import sheridan.yamazaki.businessparagon.BusinessActivity
import sheridan.yamazaki.businessparagon.firestore.FirestoreCollectionLiveData
import sheridan.yamazaki.businessparagon.firestore.FirestoreDocumentLiveData
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.User
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val application: Application
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
        private const val LIMIT = 50
    }

    private val firestore = Firebase.firestore
    private val collection = firestore.collection("users")

    override fun getUser(id: String): LiveData<User> {
        return FirestoreDocumentLiveData(collection.document(id), User::class.java)
    }

    override suspend fun insert(
        user: User
    ) {
        try {
            collection.document(user.id!!).set(user)
                .addOnSuccessListener { Log.d(TAG, "user added") }
                .addOnFailureListener{Log.d(TAG, "failed user add") }
        } catch (e: Exception){
            Log.d(TAG, "insert: ${e.message}")
        }
    }

    override suspend fun createAuthAccount(user: User, activity: Activity, auth: FirebaseAuth) {
        try {
            auth.createUserWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            val authUserId = auth.currentUser.uid
                            user.id = authUserId
                            //insert user to firestore with same id from fireauth
                            CoroutineScope(IO).launch(){
                                insert(user)
                            }
                            //start business activity
                            activity.run {
                                startActivity(Intent(this, BusinessActivity::class.java))
                                finish()
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("signInFail", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        } catch (e: Exception){
            Log.d(TAG, "insert: ${e.message}")
        }
    }

    override suspend fun signIn(email: String, password: String, activity: Activity, auth: FirebaseAuth) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        //start business activity
                        activity.run {
                            startActivity(Intent(this, BusinessActivity::class.java))
                            finish()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("SignInFail", "signInWithEmail:failure", task.exception)
                        Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                    }
                }
    }



}

/* override fun checkUser(username:String, password: String): LiveData<User> {
     val query = collection
             .whereEqualTo("username", username)
             .whereEqualTo("password", password).limit(1).get()

     return FirestoreDocumentLiveData(query.result!!.documents[0]!!.reference!!
             , User::class.java)
 }*/
