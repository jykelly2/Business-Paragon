package sheridan.yamazaki.businessparagon.ui.business.detail

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.Layout
import sheridan.yamazaki.businessparagon.model.Product
import sheridan.yamazaki.businessparagon.repository.BusinessRepository

class BusinessDetailViewModel @ViewModelInject constructor(
        private val repository: BusinessRepository
): ViewModel() {
    private val businessId = MutableLiveData<String>()

    val business: LiveData<Business> =
           businessId.switchMap{ repository.getBusiness(it) }

    fun loadData(id: String){
        businessId.value = id
    }

    val products: LiveData<List<Product>> =
            businessId.switchMap{ repository.getBusinessProducts(it) }

    var layout = MutableLiveData<Layout>()

    fun returnLayout(id: String, layoutName: String)  {
        viewModelScope.launch(Dispatchers.IO){
            val design = repository.getBusinessLayout(id, layoutName)
            layout.postValue(design.value)
        }
    }

}