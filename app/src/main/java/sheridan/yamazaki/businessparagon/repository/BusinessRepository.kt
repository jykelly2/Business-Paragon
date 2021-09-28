package sheridan.yamazaki.businessparagon.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sheridan.yamazaki.businessparagon.model.*

interface BusinessRepository {
    fun getAllBusiness(): LiveData<List<Business>>
    fun getBusiness(id: String): LiveData<Business>
    suspend fun getBusinessProductDetail(businessId: String, productId: String): LiveData<Product>
    suspend fun getBusinessLayout(businessId: String, layoutName: String): LiveData<Layout>
    suspend fun getBusinessDetailLayout(businessId: String, layoutName: String): LiveData<DetailLayout>
    suspend fun getBusinessPaymentLayout(businessId: String, layoutName: String): LiveData<PaymentLayout>
    suspend fun getBusinessShoppingListLayout(businessId: String, layoutName: String): LiveData<ShoppingListLayout>
    suspend fun getBusinessCheckoutLayout(businessId: String, layoutName: String): LiveData<CheckoutLayout>
    suspend fun getBusinessProductDetailLayout(businessId: String, layoutName: String): LiveData<ProductDetailLayout>
    fun getBusinessProducts(businessId: String): LiveData<List<Product>>
}
