package sheridan.yamazaki.businessparagon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

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