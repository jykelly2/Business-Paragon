package sheridan.yamazaki.businessparagon.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sheridan.yamazaki.businessparagon.model.Business
import sheridan.yamazaki.businessparagon.model.Layout
import sheridan.yamazaki.businessparagon.model.Product

interface BusinessRepository {
    fun getAllBusiness(): LiveData<List<Business>>
    fun getBusiness(id: String): LiveData<Business>
    suspend fun getBusinessLayout(businessId: String, layoutName: String): LiveData<Layout>
    fun getBusinessProducts(businessId: String): LiveData<List<Product>>
}
