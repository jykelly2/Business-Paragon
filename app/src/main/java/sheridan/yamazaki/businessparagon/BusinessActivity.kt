package sheridan.yamazaki.businessparagon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import sheridan.yamazaki.businessparagon.databinding.ActivityBusinessBinding
import sheridan.yamazaki.businessparagon.ui.business.list.BusinessListFragment
import sheridan.yamazaki.businessparagon.ui.business.FavouritesFragment
import sheridan.yamazaki.businessparagon.ui.business.NearMeFragment
import sheridan.yamazaki.businessparagon.ui.business.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exploreFragment = BusinessListFragment()
        val favouritesFragment = FavouritesFragment()
        val settingsFragment = SettingsFragment()
        val nearMeFragment = NearMeFragment()

        makeCurrentFragment(exploreFragment)

       binding.bottomNavigation.setOnNavigationItemSelectedListener {it ->
           when (it.itemId){
               R.id.ic_explore -> {makeCurrentFragment(exploreFragment)}
               R.id.ic_favourites -> {makeCurrentFragment(favouritesFragment)}
               R.id.ic_near_me -> {makeCurrentFragment(nearMeFragment)}
               R.id.ic_settings -> {makeCurrentFragment(settingsFragment)}
           }
           true
       }
    }

    private fun makeCurrentFragment(fragment : Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
     }



}