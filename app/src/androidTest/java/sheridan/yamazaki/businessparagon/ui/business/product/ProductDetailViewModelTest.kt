package sheridan.yamazaki.businessparagon.ui.business.product

import android.content.Context
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
class ProductDetailViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var paymentViewModel: PaymentViewModel

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context);
        viewModel = ProductDetailViewModel(BusinessRepositoryImpl(), UserRepositoryImpl())
        paymentViewModel = PaymentViewModel(BusinessRepositoryImpl(), UserRepositoryImpl())
    }

    @Test
    fun returnCorrectProduct() {
        val businessId = "1cu63CSdf8eU4dtZ4lOU"
        val productId = "MTACIUUm6gi3UrSyG6n6"

        viewModel.returnProduct(businessId,productId)
        val product = viewModel.product.getOrAwaitValueTest()
        assertThat(productId).isEqualTo(product.id)
    }

    @Test
    fun addToShoppingCartAndReturnCorrectCartSize(){
        val userId = "JSnhIMGDuXV9CvKcOtvLYirhnYf2"
        val businessId = "1cu63CSdf8eU4dtZ4lOU"
        val productId = "MTACIUUm6gi3UrSyG6n6"

        paymentViewModel.returnShoppingCart(userId)

        val products = paymentViewModel.products.getOrAwaitValueTest()
        val shoppingCarts = ArrayList<String>()
        for (item in products){
            item.shoppingCartId?.let { shoppingCarts.add(it) }
        }

        paymentViewModel.updateUserShoppingCart(userId, shoppingCarts)

        val shoppingCart = ShoppingCart(businessId, productId, 1, false)
        viewModel.addToUserShoppingCart(userId,shoppingCart)

        viewModel.returnShoppingCartSize(userId)
        val value = viewModel.cartSize.getOrAwaitValueTest()
        assertThat(value).isEqualTo(1)
    }

    @Test
    fun returnCorrectCartBusinessId() {
        val userId = "JSnhIMGDuXV9CvKcOtvLYirhnYf2"
        val businessId = "1cu63CSdf8eU4dtZ4lOU"
        val productId = "MTACIUUm6gi3UrSyG6n6"

        paymentViewModel.returnShoppingCart(userId)

        val products = paymentViewModel.products.getOrAwaitValueTest()
        val shoppingCarts = ArrayList<String>()
        for (item in products){
            item.shoppingCartId?.let { shoppingCarts.add(it) }
        }

        paymentViewModel.updateUserShoppingCart(userId, shoppingCarts)

        val shoppingCart = ShoppingCart(businessId, productId, 1, false)
        viewModel.addToUserShoppingCart(userId,shoppingCart)

        viewModel.returnCartBusinessId(userId)
        val cartBusinessId = viewModel.cartBusinessId.getOrAwaitValueTest()

        assertThat(businessId).isEqualTo(cartBusinessId)
    }

}