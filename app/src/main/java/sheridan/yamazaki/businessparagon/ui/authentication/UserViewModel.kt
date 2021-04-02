package sheridan.yamazaki.businessparagon.ui.authentication

import androidx.hilt.lifecycle.ViewModelInject
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
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
}

//val userExistance = MutableLiveData<Boolean>()

/*fun checkExistingUser(username: String, password: String) : LiveData<User>{
   // viewModelScope.launch(Dispatchers.IO){
    Log.d("jugga", repository.checkUser(username, password).toString())
     return repository.checkUser(username, password)
    //userExistance.value = repository.checkUser(username, password).value
   // }
}*/