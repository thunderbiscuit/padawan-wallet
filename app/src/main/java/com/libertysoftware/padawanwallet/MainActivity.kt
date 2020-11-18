package com.libertysoftware.padawanwallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }
//
//    fun showTutorialsFragment(view: View) {
//        Navigation.findNavController(view).navigate(R.id.action_walletFragment_to_tutorialsFragment)
//    }
//
//    fun showWalletFragment(view: View) {
//        Navigation.findNavController(view).navigate(R.id.action_tutorialsFragment_to_walletFragment)
//    }
}
