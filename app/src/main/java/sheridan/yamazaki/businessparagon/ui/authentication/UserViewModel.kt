package sheridan.yamazaki.businessparagon.ui.authentication

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.yamazaki.businessparagon.BusinessActivity
import sheridan.yamazaki.businessparagon.model.User
import sheridan.yamazaki.businessparagon.repository.UserRepository
import kotlin.math.sign

class UserViewModel @ViewModelInject constructor(
    private val repository: UserRepository
): ViewModel() {
    private val userId = MutableLiveData<String>()

    val user: LiveData<User> =  userId.switchMap{ repository.getUser(it) }

    fun createAccount(user: User, activity: Activity, auth: FirebaseAuth) {
        viewModelScope.launch(Dispatchers.IO){
            repository.createAuthAccount(user,activity,auth)
        }
    }

    fun signIn(email: String, password:String, activity: Activity, auth: FirebaseAuth) {
        viewModelScope.launch(Dispatchers.IO){
            repository.signIn(email,password,activity,auth)
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