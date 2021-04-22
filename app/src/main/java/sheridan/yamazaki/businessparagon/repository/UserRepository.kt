package sheridan.yamazaki.businessparagon.repository

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import sheridan.yamazaki.businessparagon.model.User

interface UserRepository {
    fun getUser(id: String): LiveData<User>

    suspend fun insert(
        user: User
    )

    suspend fun updateUser(
            user: User,
            activity: FragmentActivity
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
