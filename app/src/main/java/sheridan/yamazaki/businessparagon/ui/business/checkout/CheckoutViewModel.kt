package sheridan.yamazaki.businessparagon.ui.business.checkout

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.yamazaki.businessparagon.model.CheckoutLayout
import sheridan.yamazaki.businessparagon.model.Product
import sheridan.yamazaki.businessparagon.model.ProductDetailLayout
import sheridan.yamazaki.businessparagon.model.ShoppingCart
import sheridan.yamazaki.businessparagon.repository.BusinessRepository
import sheridan.yamazaki.businessparagon.repository.UserRepository

class CheckoutViewModel @ViewModelInject constructor(
        private val repository: BusinessRepository,
        private val userRepository: UserRepository
): ViewModel() {
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