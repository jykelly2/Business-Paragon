package sheridan.yamazaki.businessparagon.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.yamazaki.businessparagon.model.User
import sheridan.yamazaki.businessparagon.repository.UserRepository

class UserViewModel @ViewModelInject constructor(
    private val repository: UserRepository
): ViewModel() {

    private val userId = MutableLiveData<String>()
    val user: LiveData<User> =  userId.switchMap{ repository.getUser(it) }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO){
                repository.insert(user)
        }
    }

    /*class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }*/
}