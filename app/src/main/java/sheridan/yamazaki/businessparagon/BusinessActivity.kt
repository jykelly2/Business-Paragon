package sheridan.yamazaki.businessparagon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import sheridan.yamazaki.businessparagon.databinding.ActivityBusinessBinding
import sheridan.yamazaki.businessparagon.ui.business.FavouritesFragment
import sheridan.yamazaki.businessparagon.ui.business.SettingsFragment
import sheridan.yamazaki.businessparagon.ui.business.analytic.AnalyticFragment
import sheridan.yamazaki.businessparagon.ui.business.list.BusinessListFragment

@AndroidEntryPoint
class BusinessActivity : AppCompatActivity(){ //, OnMapReadyCallback {
    private lateinit var binding: ActivityBusinessBinding
    private var savedView : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null){
            savedView = savedInstanceState?.getString("savedView").toString()
        }

        val fromChatbotView = intent?.getStringExtra("chatbot")

        if (fromChatbotView != null){
            savedView = fromChatbotView
        }
        binding = ActivityBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exploreFragment = BusinessListFragment()
        val favouritesFragment = FavouritesFragment()
        val settingsFragment = SettingsFragment()
        val analyticFragment = AnalyticFragment()

        when (savedView) {
            "analytic" -> {
                makeCurrentFragment(analyticFragment)
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
               R.id.analyticFragment -> {
                   savedView = "analytic"
                   makeCurrentFragment(analyticFragment)
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




