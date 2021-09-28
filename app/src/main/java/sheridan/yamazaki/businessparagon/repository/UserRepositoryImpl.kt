package sheridan.yamazaki.businessparagon.repository

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import sheridan.yamazaki.businessparagon.BusinessActivity
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.firestore.FirestoreDocumentLiveData
import sheridan.yamazaki.businessparagon.model.Product
import sheridan.yamazaki.businessparagon.model.ShoppingCart
import sheridan.yamazaki.businessparagon.model.User
import sheridan.yamazaki.businessparagon.ui.business.SettingsFragment
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
    private val businessCollection = firestore.collection("businesses")

    override fun getUser(id: String): LiveData<User> {
        return FirestoreDocumentLiveData(collection.document(id), User::class.java)
    }

    override suspend fun updateUserShoppingCart(userId: String, shoppingCartIds: ArrayList<String>) {
        for (item in shoppingCartIds){
            Log.d("updateshopi", item)
            collection.document(userId).collection("cart")
                    .document(item).update("processed", true)
        }
    }
    override suspend fun getUserShoppingCart(id: String): LiveData<List<Product>> {
        val cartCollection = collection.document(id).collection("cart").get().await()
        var carts = ArrayList<ShoppingCart>()
        for (singleCart in cartCollection) {
            carts.add(singleCart.toObject<ShoppingCart>())
        }

        var products = ArrayList<Product>()
        for (cart in carts) {
            if (cart.processed == false) {
                var product = cart.businessId?.let {
                    cart.productId?.let { it1 ->
                        businessCollection.document(it).collection("products")
                                .document(it1).get().await().toObject<Product>()
                    }
                }

                if (product != null) {
                    product.quantity = cart.quantity
                    product.shoppingCartId = cart.id
                    products.add(product)
                }
            }
        }
        return MutableLiveData(products)
    }

    override suspend fun getCartBusinessId(id: String): LiveData<String> {
        val cart = collection.document(id).collection("cart").whereEqualTo("processed", false).limit(1).get().await().documents[0].toObject<ShoppingCart>()
        val businessId = cart?.businessId
        return MutableLiveData(businessId)
    }

    override suspend fun getUserShoppingCartSize(id: String): LiveData<Int> {
        val cartSize = collection.document(id).collection("cart").whereEqualTo("processed", false).get().await().documents.size
        return MutableLiveData(cartSize)
    }

    override suspend fun addToUserShoppingCart(userId: String, shoppingCart: ShoppingCart) {
        try {
            collection.document(userId).collection("cart").add(shoppingCart)
        }catch (e: Exception){
            Log.d(TAG, "update: ${e.message}")
        }
    }

    override suspend fun removeFromUserShoppingCart(userId: String, shoppingCartId: String) {
        try {
            collection.document(userId).collection("cart").document(shoppingCartId).delete()
        }catch (e: Exception){
            Log.d(TAG, "update: ${e.message}")
        }
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

    override suspend fun updateUser(user: User, activity: FragmentActivity) {
        try {
            collection.document(user.id!!).set(user)
            updateFireAuthAccount(user, activity)
        }catch (e: Exception){
            Log.d(TAG, "update: ${e.message}")
        }
    }

    override suspend fun updateUserBillingInfo(user: User) {
        try {
            collection.document(user.id!!).set(user)
        }catch (e: Exception){
            Log.d(TAG, "update: ${e.message}")
        }
    }

    private fun updateFireAuthAccount(newProfile: User, activity: FragmentActivity){
        val user = Firebase.auth.currentUser
        user!!.updateEmail(newProfile.email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User email address updated.")
                    }
                    else {
                        Log.d(TAG, "updateWithEmail:failure", task.exception)
                    }
                }
        user!!.updatePassword(newProfile.password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Updated Profile.",
                                Toast.LENGTH_SHORT).show()
                        val fragment = SettingsFragment()
                        activity.supportFragmentManager?.beginTransaction()?.apply {
                            replace(R.id.fl_wrapper, fragment)
                            commit()
                        }
                    }
                    else {
                        Log.d(TAG, "update password:failure", task.exception)
                    }
                }
    }


    override suspend fun createAuthAccount(user: User, activity: Activity, auth: FirebaseAuth) {
        try {
            Log.d("infireauth", user.email + user.password)
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
