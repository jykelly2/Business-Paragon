package sheridan.yamazaki.businessparagon.repository

import androidx.lifecycle.LiveData
import sheridan.yamazaki.businessparagon.model.User

interface UserRepository {
    //fun getAllUsers(): LiveData<List<User>>
    fun getUser(id: String): LiveData<User>

    //fun checkUser(username: String, password: String): LiveData<User>

    suspend fun insert(
        user: User
    )
}