package com.libertysoftware.padawanwallet.drawer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.libertysoftware.padawanwallet.R

class DrawerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        val drawerItem: String? = intent.getStringExtra("drawerItem")
        when(drawerItem) {
            "about" -> supportFragmentManager.beginTransaction().replace(R.id.fragment_about, AboutFragment()).commit()
            "settings" -> supportFragmentManager.beginTransaction().replace(R.id.fragment_about, SettingsFragment()).commit()
        }
    }
}
