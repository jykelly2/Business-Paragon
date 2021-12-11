package sheridan.yamazaki.businessparagon.ui.business.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.User
import sheridan.yamazaki.businessparagon.repository.BusinessRepository
import sheridan.yamazaki.businessparagon.repository.UserRepository

class BusinessListViewModel @ViewModelInject constructor(
        private val repository: BusinessRepository,
        private val userRepository: UserRepository
): ViewModel() {
    val businesses: LiveData<List<Business>> = repository.getAllBusiness()
    private val userId = MutableLiveData<String>()
    val user: LiveData<User> =  userId.switchMap{ userRepository.getUser(it) }
    fun loadUserData(id: String){
        userId.value = id
    }

    var cartSize = MutableLiveData<Int>()

    fun returnShoppingCartSize(userId: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val size = userRepository.getUserShoppingCartSize(userId)
            cartSize.postValue(size.value)
        }
    }

    var cartBusinessId = MutableLiveData<String>()

    fun returnCartBusinessId(userId: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val businessId = userRepository.getCartBusinessId(userId)
            cartBusinessId.postValue(businessId.value)
        }
    }
}
