package sheridan.yamazaki.businessparagon.ui.business.detail

import android.content.Context
import org.junit.Assert.*
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import com.google.common.truth.Truth.assertThat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.matchers.JUnitMatchers.*
import org.junit.runner.manipulation.Ordering
import sheridan.yamazaki.businessparagon.BusinessActivity
import sheridan.yamazaki.businessparagon.MainCoroutineRule
import sheridan.yamazaki.businessparagon.getOrAwaitValueTest
import sheridan.yamazaki.businessparagon.model.ShoppingCart
import sheridan.yamazaki.businessparagon.repository.*
import sheridan.yamazaki.businessparagon.ui.business.payment.PaymentViewModel
import sheridan.yamazaki.businessparagon.ui.business.product.ProductDetailViewModel

@ExperimentalCoroutinesApi
class BusinessDetailViewModelTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: BusinessDetailViewModel

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context);
        viewModel = BusinessDetailViewModel(BusinessRepositoryImpl(), UserRepositoryImpl())
    }

    @Test
    fun returnCorrectBusiness(){
        val businessId = "1cu63CSdf8eU4dtZ4lOU"

        viewModel.loadData(businessId)
        val business = viewModel.business.getOrAwaitValueTest()
        assertThat(businessId).isEqualTo(business.id)
    }

    @Test
    fun businessProductsIsNotEmpty() {
        val businessId = "1cu63CSdf8eU4dtZ4lOU"
        viewModel.loadData(businessId)
        val products = viewModel.products.getOrAwaitValueTest()
        assertThat(products.size).isGreaterThan(0)
    }

    @Test
    fun returnCorrectBusinessDesignLayout() {
        val businessId = "1cu63CSdf8eU4dtZ4lOU"
        val layoutName = "detail"
        viewModel.returnLayout(businessId, layoutName)
        val layout = viewModel.layout.getOrAwaitValueTest()
        assertThat(layoutName).isEqualTo(layout.name)
    }

}