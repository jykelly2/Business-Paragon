package sheridan.yamazaki.businessparagon.repository

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.Product
import sheridan.yamazaki.businessparagon.model.ShoppingCart
import sheridan.yamazaki.businessparagon.model.User

interface UserRepository {
    fun getUser(id: String): LiveData<User>
    suspend fun getUserShoppingCart(id: String): LiveData<List<Product>>
    suspend fun getCartBusinessId(id: String): LiveData<String>
    suspend fun getUserShoppingCartSize(id: String): LiveData<Int>
    suspend fun removeFromUserShoppingCart(
            userId: String,
            shoppingCartId: String
    )
    suspend fun addToUserShoppingCart(
            userId: String,
           shoppingCart: ShoppingCart
    )

    suspend fun updateUserShoppingCart(
            userId: String,
            shoppingCartIds: ArrayList<String>
    )
    suspend fun insert(
        user: User
    )

    suspend fun updateUser(
            user: User,
            activity: FragmentActivity
    )

    suspend fun updateUserBillingInfo(
            user: User
    )

    suspend fun createAuthAccount(
            user: User,
            activity: Activity,
            auth: FirebaseAuth
    )

    suspend fun signIn(
            email: String,
            password: String,
            activity: Activity,
            auth: FirebaseAuth
    )
}
//fun checkUser(username: String, password: String): LiveData<User>
