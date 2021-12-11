package sheridan.yamazaki.businessparagon.ui.business.payment

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.yamazaki.businessparagon.model.*
import sheridan.yamazaki.businessparagon.repository.BusinessRepository
import sheridan.yamazaki.businessparagon.repository.UserRepository

class PaymentViewModel @ViewModelInject constructor(
        private val repository: BusinessRepository,
        private val userRepository: UserRepository
): ViewModel() {

    private val businessId = MutableLiveData<String>()

    val business: LiveData<Business> =
            businessId.switchMap{ repository.getBusiness(it) }

    fun loadData(id: String){
        businessId.value = id
    }

    private val userId = MutableLiveData<String>()
    val user: LiveData<User> =  userId.switchMap{ userRepository.getUser(it) }
    fun loadUserData(id: String){
        userId.value = id
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO){
            userRepository.updateUserBillingInfo(user)
        }
    }

    fun updateBusinessProductStock(businessId: String, product:Product, quantity:Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateBusinessProductStockData(businessId,product,quantity)
        }
    }

    fun updateUserShoppingCart(userId: String, shoppingCartIds: ArrayList<String>){
        viewModelScope.launch(Dispatchers.IO){
            userRepository.updateUserShoppingCart(userId,shoppingCartIds)
        }
    }

    var products = MutableLiveData<List<Product>>()

    fun returnShoppingCart(userId: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val productDetail = userRepository.getUserShoppingCart(userId)
            products.postValue(productDetail.value)
        }
    }

    var layout = MutableLiveData<PaymentLayout>()

    fun returnLayout(id: String, layoutName: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val design = repository.getBusinessPaymentLayout(id, layoutName)
            layout.postValue(design.value)
        }
    }
}