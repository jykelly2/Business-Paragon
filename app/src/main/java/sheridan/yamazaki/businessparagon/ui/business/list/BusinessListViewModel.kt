package sheridan.yamazaki.businessparagon.ui.business.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.repository.BusinessRepository

class BusinessListViewModel @ViewModelInject constructor(
        private val repository: BusinessRepository
): ViewModel() {
    val businesses: LiveData<List<Business>> = repository.getAllBusiness()
}
