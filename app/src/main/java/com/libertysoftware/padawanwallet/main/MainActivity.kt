package com.libertysoftware.padawanwallet.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.libertysoftware.padawanwallet.R

class MainActivity : AppCompatActivity() {

    // lateinit var tabLayout: TabLayout
    // lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // tabLayout = findViewById(R.id.tabLayout)
        // viewPager = findViewById(R.id.pager)
        // viewPager.adapter = AppViewPagerAdapter(supportFragmentManager, lifecycle)

//        TabLayoutMediator(tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
//            when (position) {
//                0 -> { tab.text = "TAB ONE" }
//                1 -> { tab.text = "TAB TWO" }
//            }
//        }).attach()

//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            when (position) {
//                0 -> { tab.text = "TAB ONE" }
//                1 -> { tab.text = "TAB TWO" }
//            }
//        }.attach()
    }
}
