/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.drawer

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.ActivityDrawerBinding

class DrawerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = findViewById(R.id.toolbarDrawerActivity)
        setSupportActionBar(toolbar)
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerActivityLayout,
            R.string.open,
            R.string.close,
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                findViewById<TextView>(R.id.versionName).text = BuildConfig.VERSION_NAME
            }
        }
        binding.drawerActivityLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_name)

        // open the right fragment when starting the activity
        when(intent.getStringExtra("drawerItem")) {
            "about" -> supportFragmentManager.beginTransaction().replace(R.id.fragmentAbout, AboutFragment()).commit()
            "settings" -> supportFragmentManager.beginTransaction().replace(R.id.fragmentAbout, SettingsFragment()).commit()
            "seedphrase" -> supportFragmentManager.beginTransaction().replace(R.id.fragmentAbout, SeedPhraseFragment()).commit()
            "sendCoinsBack" -> supportFragmentManager.beginTransaction().replace(R.id.fragmentAbout, SendCoinsBackFragment()).commit()
        }

        binding.navViewDrawerActivity.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.about -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentAbout, AboutFragment()).commit()
                    binding.drawerActivityLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.settings -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentAbout, SettingsFragment()).commit()
                    binding.drawerActivityLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.mnemonic -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentAbout, SeedPhraseFragment()).commit()
                    binding.drawerActivityLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.sendCoinsBack -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentAbout, SendCoinsBackFragment()).commit()
                    binding.drawerActivityLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.wallet -> {
                    // startActivity(Intent(this, HomeActivity::class.java))
                    binding.drawerActivityLayout.closeDrawer(GravityCompat.START)
                    finish()
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerActivityLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerActivityLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerActivityLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
