package com.libertysoftware.padawanwallet.main

/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.pager.adapter = MainPagerAdapter(this)
        tabLayout = binding.tabLayout
        viewPager = binding.pager

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> { tab.text = "Wallet" }
                1 -> { tab.text = "Tutorials" }
            }
        }.attach()
        
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.item1 -> Toast.makeText(applicationContext, "About screen clicked", Toast.LENGTH_SHORT).show()
                R.id.item2 -> Toast.makeText(applicationContext, "Test bdk screen clicked", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): kotlin.Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
