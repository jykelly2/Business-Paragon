package sheridan.yamazaki.businessparagon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.firestore.FirestoreActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
/* val navHostFragment =
     supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
 navController = navHostFragment.navController
 appBarConfiguration = AppBarConfiguration(navController.graph)
 setupActionBarWithNavController(navController, appBarConfiguration)*/

/*override fun onSupportNavigateUp(): Boolean {
     val navController = findNavController(R.id.nav_host_fragment)
     return navController.navigateUp(appBarConfiguration)
             || super.onSupportNavigateUp()
 }*/