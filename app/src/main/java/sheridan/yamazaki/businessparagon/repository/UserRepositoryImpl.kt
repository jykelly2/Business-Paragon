package sheridan.yamazaki.businessparagon.repository

import android.app.Application
import android.app.PendingIntent.getActivity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
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

    private val firestore = Firebase.firestore//FirebaseFirestore.getInstance()
    private val collection = firestore.collection("users")

    /*override fun getAllUsers(): LiveData<List<User>> {
        return FirestoreCollectionLiveData(query, User::class.java)
    }*/

    override fun getUser(id: String): LiveData<User> {
        return FirestoreDocumentLiveData(collection.document(id), User::class.java)
    }

    override fun checkUser(username:String, password: String): LiveData<User> {
        Log.d(TAG, "herheh")

        val query = collection
                .whereEqualTo("username", username)
                .whereEqualTo("password", password).limit(1).get()

        return FirestoreDocumentLiveData(query.result!!.documents[0]!!.reference!!
                , User::class.java)
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

}

