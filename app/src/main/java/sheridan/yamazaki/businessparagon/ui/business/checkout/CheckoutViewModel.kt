package sheridan.yamazaki.businessparagon.ui.business.checkout

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.yamazaki.businessparagon.model.*
import sheridan.yamazaki.businessparagon.repository.BusinessRepository
import sheridan.yamazaki.businessparagon.repository.UserRepository

class CheckoutViewModel @ViewModelInject constructor(
        private val repository: BusinessRepository,
        private val userRepository: UserRepository
): ViewModel() {

    private val businessId = MutableLiveData<String>()

    val business: LiveData<Business> =
            businessId.switchMap{ repository.getBusiness(it) }

    fun loadData(id: String){
        businessId.value = id
    }

    var products = MutableLiveData<List<Product>>()

    fun returnShoppingCart(userId: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val productDetail = userRepository.getUserShoppingCart(userId)
            products.postValue(productDetail.value)
        }
    }

    fun removeFromUserShoppingCart(userId: String, shoppingCartId: String){
        viewModelScope.launch(Dispatchers.IO){
            userRepository.removeFromUserShoppingCart(userId,shoppingCartId)
        }
    }

    var layout = MutableLiveData<CheckoutLayout>()

    fun returnLayout(id: String, layoutName: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val design = repository.getBusinessCheckoutLayout(id, layoutName)
            layout.postValue(design.value)
        }
    }
}