package sheridan.yamazaki.businessparagon.ui.business.checkout
import android.content.Context
import android.util.Log
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
class CheckoutViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CheckoutViewModel
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var productDetailViewModel: ProductDetailViewModel

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context);
        viewModel = CheckoutViewModel(BusinessRepositoryImpl(), UserRepositoryImpl())
        paymentViewModel = PaymentViewModel(BusinessRepositoryImpl(), UserRepositoryImpl())
        productDetailViewModel = ProductDetailViewModel(BusinessRepositoryImpl(), UserRepositoryImpl())
    }

    @Test
    fun removeItemFromShoppingCartCorrectly(){
        val userId = "JSnhIMGDuXV9CvKcOtvLYirhnYf2"
        val businessId = "1cu63CSdf8eU4dtZ4lOU"
        val productId = "MTACIUUm6gi3UrSyG6n6"

        //Get User Shopping cart and clear items
        paymentViewModel.returnShoppingCart(userId)

        val products = paymentViewModel.products.getOrAwaitValueTest()
        val shoppingCarts = ArrayList<String>()
        for (item in products){
            item.shoppingCartId?.let { shoppingCarts.add(it) }
        }
        paymentViewModel.updateUserShoppingCart(userId, shoppingCarts)

        //Add to item to user shopping cart
        val shoppingCart = ShoppingCart(businessId, productId, 1, false)
        productDetailViewModel.addToUserShoppingCart(userId,shoppingCart)

        //Get User Shopping cart and remove the newly added product
        viewModel.returnShoppingCart(userId)
        val addedProduct = viewModel.products.getOrAwaitValueTest()
        val addedShoppingCart = ArrayList<String>()
        for (item in addedProduct){
            item.shoppingCartId?.let { addedShoppingCart.add(it) }
        }

        viewModel.removeFromUserShoppingCart(userId, addedShoppingCart[0])

        //Get User Shopping cart size and expect it to be size 0
        productDetailViewModel.returnShoppingCartSize(userId)
        val value = productDetailViewModel.cartSize.getOrAwaitValueTest()
        assertThat(value).isEqualTo(0)
    }



}