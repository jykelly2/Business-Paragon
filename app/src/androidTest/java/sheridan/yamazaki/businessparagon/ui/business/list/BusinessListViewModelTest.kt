package sheridan.yamazaki.businessparagon.ui.business.list

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
class BusinessListViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: BusinessListViewModel

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context);
        viewModel = BusinessListViewModel(BusinessRepositoryImpl(), UserRepositoryImpl())
    }

    @Test
    fun businessesIsNotEmpty(){
        val businesses = viewModel.businesses.getOrAwaitValueTest()
        assertThat(businesses.size).isGreaterThan(0)
    }
}