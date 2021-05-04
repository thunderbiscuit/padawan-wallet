/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.ActivityHomeBinding
import com.goldenraven.padawanwallet.drawer.DrawerActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber

private const val CAMERA_REQUEST_CODE = 101

class HomeActivity : AppCompatActivity() {

    // lateinit var toggle: ActionBarDrawerToggle
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setupPermissions()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.pager.adapter = HomePagerAdapter(this)
        tabLayout = binding.tabLayout
        viewPager = binding.pager

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> { tab.text = "Wallet" }
                1 -> { tab.text = "Tutorials" }
            }
        }.attach()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_name)

        val intentAbout: Intent = Intent(this, DrawerActivity::class.java).putExtra("drawerItem", "about")
        val intentSettings: Intent = Intent(this, DrawerActivity::class.java).putExtra("drawerItem", "settings")
        val intentSeedPhrase: Intent = Intent(this, DrawerActivity::class.java).putExtra("drawerItem", "seedphrase")


        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.about -> {
                    Timber.i("[PADAWANLOGS] clicked about")
                    startActivity(intentAbout)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.settings -> {
                    Timber.i("[PADAWANLOGS] clicked setting")
                    startActivity(intentSettings)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.mnemonic -> {
                    Timber.i("[PADAWANLOGS] clicked seed phrase")
                    startActivity(intentSeedPhrase)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.wallet -> {
                    Timber.i("[PADAWANLOGS] clicked seed phrase")
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    Timber.i("[PADAWANLOGS] Drawer selection didn't work properly")
                    true
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupPermissions() {
        Timber.i("[PADAWANLOGS] Requesting permission step 1")
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        // val permission = checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
        Timber.i("[PADAWANLOGS] Requesting permission step 2: $permission")

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Timber.i("[PADAWANLOGS] Requesting permission step 3, making request")
            makeRequest()
        }
    }

    private fun makeRequest() {
        Timber.i("[PADAWANLOGS] Requesting permission step 5")
        // requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need the camera permission to be able to use this feature", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Thank you!", Toast.LENGTH_LONG).show()
                    Timber.i("[PADAWANLOGS] Camera permission successful")
                }
            }
        }
    }
}
