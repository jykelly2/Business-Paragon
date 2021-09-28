package sheridan.yamazaki.businessparagon.ui.business.location

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.repository.BusinessRepository

class LocationViewModel @ViewModelInject constructor(
    private val repository: BusinessRepository
): ViewModel() {
    val businesses: LiveData<List<Business>> = repository.getAllBusiness()
}