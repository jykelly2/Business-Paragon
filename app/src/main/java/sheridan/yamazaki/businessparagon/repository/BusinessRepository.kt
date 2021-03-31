package sheridan.yamazaki.businessparagon.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sheridan.yamazaki.businessparagon.model.Business

interface BusinessRepository {
    fun getAllBusiness(): LiveData<List<Business>>
    fun getBusiness(id: String): LiveData<Business>
}
