package sheridan.yamazaki.businessparagon.ui.business.detail

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.DetailLayout
import sheridan.yamazaki.businessparagon.model.Layout
import sheridan.yamazaki.businessparagon.model.Product
import sheridan.yamazaki.businessparagon.repository.BusinessRepository
import sheridan.yamazaki.businessparagon.repository.UserRepository

class BusinessDetailViewModel @ViewModelInject constructor(
        private val repository: BusinessRepository,
        private val userRepository: UserRepository
): ViewModel() {
    private val businessId = MutableLiveData<String>()

    val business: LiveData<Business> =
           businessId.switchMap{ repository.getBusiness(it) }

    fun loadData(id: String){
        businessId.value = id
    }

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

    val products: LiveData<List<Product>> =
            businessId.switchMap{ repository.getBusinessProducts(it) }

    var layout = MutableLiveData<DetailLayout>()

    fun returnLayout(id: String, layoutName: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val design = repository.getBusinessDetailLayout(id, layoutName)
            layout.postValue(design.value)
        }
    }

}