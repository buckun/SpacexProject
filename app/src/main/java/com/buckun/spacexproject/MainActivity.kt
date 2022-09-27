package com.buckun.spacexproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.buckun.spacexproject.base.BaseActivity
import com.buckun.spacexproject.databinding.ActivityMainBinding
import com.buckun.spacexproject.ui.favoriteRockets.FavoriteRocketFragment
import com.buckun.spacexproject.ui.spacexrockets.SpaceXRocketsFragment
import com.buckun.spacexproject.ui.upcomingRockets.UpcomingLaunchesFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : BaseActivity<ActivityMainBinding, HomeViewModel>() {

    private var currentLoadedFragment: Fragment? = null

    override fun getContentView(): Int = R.layout.activity_main
    override fun setViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java
    override fun initViews(savedInstanceState: Bundle?) {
        setListeners()
        loadFragment(SpaceXRocketsFragment())
    }

    private fun setListeners() {
        viewDataBinding?.tlHome?.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.position) {
                    0 -> {
                        loadFragment(SpaceXRocketsFragment())
                    }
                    1 -> {
                        loadFragment(FavoriteRocketFragment())
                    }
                    2 -> {
                        loadFragment(UpcomingLaunchesFragment())
                    }

                }

            }
        })
    }

    fun loadFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        currentLoadedFragment = fragment
        val fragmentTransaction = ft.replace(R.id.flContainer, fragment)
        fragmentTransaction.commit()
    }
}