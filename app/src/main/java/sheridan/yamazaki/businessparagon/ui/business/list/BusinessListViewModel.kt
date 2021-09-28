package sheridan.yamazaki.businessparagon.ui.business.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.repository.BusinessRepository
import sheridan.yamazaki.businessparagon.repository.UserRepository

class BusinessListViewModel @ViewModelInject constructor(
        private val repository: BusinessRepository,
        private val userRepository: UserRepository
): ViewModel() {
    val businesses: LiveData<List<Business>> = repository.getAllBusiness()

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
