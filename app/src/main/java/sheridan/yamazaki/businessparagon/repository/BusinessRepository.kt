package sheridan.yamazaki.businessparagon.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.Layout

interface BusinessRepository {
    fun getAllBusiness(): LiveData<List<Business>>
    fun getBusiness(id: String): LiveData<Business>
    suspend fun getBusinessLayout(id: String, layoutName: String): LiveData<Layout>
}
