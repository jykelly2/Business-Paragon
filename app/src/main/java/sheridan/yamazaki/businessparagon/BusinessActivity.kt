package sheridan.yamazaki.businessparagon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import sheridan.yamazaki.businessparagon.databinding.ActivityBusinessBinding
import sheridan.yamazaki.businessparagon.ui.business.list.BusinessListFragment
import sheridan.yamazaki.businessparagon.ui.business.FavouritesFragment
import sheridan.yamazaki.businessparagon.ui.business.NearMeFragment
import sheridan.yamazaki.businessparagon.ui.business.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)


       /*  val navHostFragment =
          supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
      navController = navHostFragment.navController
      appBarConfiguration = AppBarConfiguration(navController.graph)
      setupActionBarWithNavController(navController, appBarConfiguration)*/

       /* val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)*/


        val exploreFragment = BusinessListFragment()
        val favouritesFragment = FavouritesFragment()
        val settingsFragment = SettingsFragment()
        val nearMeFragment = NearMeFragment()

        makeCurrentFragment(exploreFragment)

       binding.bottomNavigation.setOnNavigationItemSelectedListener {it ->
           when (it.itemId){
               R.id.businessListFragment-> {makeCurrentFragment(exploreFragment)}
               R.id.favouriteFragment -> {makeCurrentFragment(favouritesFragment)}
               R.id.nearMeFragment -> {makeCurrentFragment(nearMeFragment)}
               R.id.settingsFragment -> {makeCurrentFragment(settingsFragment)}
           }
           true
       }
    }

   /* override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/



    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }
    private fun navigate (name : String) {
        findNavController(R.id.businessListFragment).navigate(R.id.action_businessActivity_to_businessListFragment)

    }*/

    private fun makeCurrentFragment(fragment : Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
     }
}