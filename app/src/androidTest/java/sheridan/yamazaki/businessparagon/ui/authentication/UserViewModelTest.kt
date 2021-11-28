package sheridan.yamazaki.businessparagon.ui.authentication

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
import sheridan.yamazaki.businessparagon.ui.business.list.BusinessListViewModel
import sheridan.yamazaki.businessparagon.ui.business.payment.PaymentViewModel
import sheridan.yamazaki.businessparagon.ui.business.product.ProductDetailViewModel

@ExperimentalCoroutinesApi
class UserViewModelTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context);
        viewModel = UserViewModel(UserRepositoryImpl())
    }

    @Test
    fun returnCorrectUser(){
        val userId = "JSnhIMGDuXV9CvKcOtvLYirhnYf2"
        viewModel.loadData(userId)
        val user = viewModel.user.getOrAwaitValueTest()
        assertThat(userId).isEqualTo(user.id)
    }
}