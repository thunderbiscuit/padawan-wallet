package com.libertysoftware.padawanwallet.drawer

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.ActivityDrawerBinding
import com.libertysoftware.padawanwallet.main.HomeActivity
import timber.log.Timber

class DrawerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar: Toolbar = findViewById(R.id.toolbarDrawerActivity)
        setSupportActionBar(toolbar)
        val drawerToggle = ActionBarDrawerToggle(this, binding.drawerActivityLayout, R.string.open, R.string.close)
        binding.drawerActivityLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_name)

        // open the right fragment when starting the activity
        val drawerItem: String? = intent.getStringExtra("drawerItem")
        when(drawerItem) {
            "about" -> supportFragmentManager.beginTransaction().replace(R.id.fragment_about, AboutFragment()).commit()
            "settings" -> supportFragmentManager.beginTransaction().replace(R.id.fragment_about, SettingsFragment()).commit()
            "seedphrase" -> supportFragmentManager.beginTransaction().replace(R.id.fragment_about, SeedPhraseFragment()).commit()
        }

        binding.navViewDrawerActivity.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.about -> {
                    Timber.i("clicked about")
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_about, AboutFragment()).commit()
                    binding.drawerActivityLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.settings -> {
                    Timber.i("clicked setting")
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_about, SettingsFragment()).commit()
                    binding.drawerActivityLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.mnemonic -> {
                    Timber.i("clicked seed phrase")
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_about, SeedPhraseFragment()).commit()
                    binding.drawerActivityLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.wallet -> {
                    Timber.i("clicked seed phrase")
                    startActivity(Intent(this, HomeActivity::class.java))
                    binding.drawerActivityLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    Timber.i("Drawer selection didn't work properly")
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
