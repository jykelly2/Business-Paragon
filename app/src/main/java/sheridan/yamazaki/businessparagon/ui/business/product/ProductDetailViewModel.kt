package sheridan.yamazaki.businessparagon.ui.business.product

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.yamazaki.businessparagon.model.*
import sheridan.yamazaki.businessparagon.repository.BusinessRepository
import sheridan.yamazaki.businessparagon.repository.UserRepository

class ProductDetailViewModel @ViewModelInject constructor(
    private val repository: BusinessRepository,
    private val userRepository: UserRepository
): ViewModel() {
    var cartBusinessId = MutableLiveData<String>()

    fun returnCartBusinessId(userId: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val businessId = userRepository.getCartBusinessId(userId)
            cartBusinessId.postValue(businessId.value)
        }
    }

    var cartSize = MutableLiveData<Int>()

    fun returnShoppingCartSize(userId: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val size = userRepository.getUserShoppingCartSize(userId)
            cartSize.postValue(size.value)
        }
    }

    fun addToUserShoppingCart(userId: String, shoppingCart: ShoppingCart){
        viewModelScope.launch(Dispatchers.IO){
            userRepository.addToUserShoppingCart(userId,shoppingCart)
        }
    }

    var product = MutableLiveData<Product>()

    fun returnProduct(businessId: String, productId: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val productDetail = repository.getBusinessProductDetail(businessId, productId)
            product.postValue(productDetail.value)
        }
    }

    var layout = MutableLiveData<ProductDetailLayout>()

    fun returnLayout(id: String, layoutName: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val design = repository.getBusinessProductDetailLayout(id, layoutName)
            layout.postValue(design.value)
        }
    }

}