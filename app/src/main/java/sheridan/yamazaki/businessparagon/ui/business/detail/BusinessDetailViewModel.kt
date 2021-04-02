package sheridan.yamazaki.businessparagon.ui.business.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import sheridan.yamazaki.businessparagon.model.Business
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
}