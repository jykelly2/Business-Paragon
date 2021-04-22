package sheridan.yamazaki.businessparagon

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.databinding.ActivityBusinessBinding
import sheridan.yamazaki.businessparagon.ui.business.FavouritesFragment
import sheridan.yamazaki.businessparagon.ui.business.NearMeFragment
import sheridan.yamazaki.businessparagon.ui.business.SettingsFragment
import sheridan.yamazaki.businessparagon.ui.business.list.BusinessListFragment

@AndroidEntryPoint
class BusinessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusinessBinding
    private var savedView : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null){
            savedView = savedInstanceState?.getString("savedView").toString()
        }

        binding = ActivityBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exploreFragment = BusinessListFragment()
        val favouritesFragment = FavouritesFragment()
        val settingsFragment = SettingsFragment()
        val nearMeFragment = NearMeFragment()

        when (savedView) {
            "nearMe" -> {
                makeCurrentFragment(nearMeFragment)
            }
            "favourite" -> {
                makeCurrentFragment(favouritesFragment)
            }
            "settings" -> {
                makeCurrentFragment(settingsFragment)
            }
            else -> { makeCurrentFragment(exploreFragment)}
        }

       binding.bottomNavigation.setOnNavigationItemSelectedListener { it ->
           when (it.itemId){
               R.id.businessListFragment -> {
                   savedView = "explore"
                   makeCurrentFragment(exploreFragment)
               }
               R.id.favouriteFragment -> {
                   savedView = "favourite"
                   makeCurrentFragment(favouritesFragment)
               }
               R.id.nearMeFragment -> {
                   savedView = "nearMe"
                   makeCurrentFragment(nearMeFragment)
               }
               R.id.settingsFragment -> {
                   savedView = "settings"
                   makeCurrentFragment(settingsFragment)
               }
           }
           true
       }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
     }

    override fun onSaveInstanceState(outState: Bundle) {
        if (outState != null) {
            super.onSaveInstanceState(outState)
        }
        outState?.putString("savedView", savedView)
    }
}
/*  val navHostFragment =
       supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
   navController = navHostFragment.navController
   appBarConfiguration = AppBarConfiguration(navController.graph)
   setupActionBarWithNavController(navController, appBarConfiguration)*/

/* val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
 val navController = findNavController(R.id.fragment)

 bottomNavigationView.setupWithNavController(navController)*/

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