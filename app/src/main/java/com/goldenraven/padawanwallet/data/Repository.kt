/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data

import android.content.SharedPreferences
import android.util.Log
import com.goldenraven.padawanwallet.utils.RequiredInitialWalletData

private const val TAG = "Repository"

object Repository {

    private lateinit var sharedPreferences: SharedPreferences
    // private var resetTutorial : Boolean = false
    fun setSharedPreferences(sharedPref: SharedPreferences) {
        sharedPreferences = sharedPref
    }

    fun doesWalletExist(): Boolean {
        // val currentWallet: SharedPreferences = applicationContext.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
        val currentWalletExists: Boolean = sharedPreferences.getBoolean("initialized", false)
        Log.i(TAG, "Value of currentWalletExists at launch: $currentWalletExists")
        return currentWalletExists
    }

    fun getInitialWalletData(): RequiredInitialWalletData {
        // val currentWallet: SharedPreferences = applicationContext.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
        val descriptor: String = sharedPreferences.getString("descriptor", null)!!
        val changeDescriptor: String = sharedPreferences.getString("changeDescriptor", null)!!
        return RequiredInitialWalletData(descriptor, changeDescriptor)
    }

    fun saveWallet(path: String, descriptor: String, changeDescriptor: String) {
        Log.i(TAG, "Saved wallet: path -> $path, descriptor -> $descriptor, change descriptor -> $changeDescriptor")
        sharedPreferences.edit().apply {
            putBoolean("initialized", true)
            putString("path", path)
            putString("descriptor", descriptor)
            putString("changeDescriptor", changeDescriptor)
        }.apply()
    }

    fun saveMnemonic(mnemonic: String) {
        Log.i(TAG,"The seed phrase is: $mnemonic")
        val editor = sharedPreferences.edit()
        editor.putString("mnemonic", mnemonic)
        editor.apply()
    }

    fun getMnemonic(): String {
        return sharedPreferences.getString("mnemonic", "No seed phrase saved") ?: "Seed phrase not there"
    }

    fun wasFaucetCallDone(): Boolean {
        return sharedPreferences.getBoolean("faucetCallDone", false)
    }

    fun faucetCallDone() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("faucetCallDone", true)
        editor.apply()
    }

    fun offerFaucetDone() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("offerFaucetDone", true)
        editor.apply()
    }

    fun didWeOfferFaucet(): Boolean {
        return sharedPreferences.getBoolean("offerFaucetDone", false)
    }

    // fun loadTutorialsDone(): MutableMap<String, Boolean> {
    //     if (sharedPreferences.getBoolean("firstTimeTutorialsDone", true)) {
    //         sharedPreferences.edit().apply {
    //             putBoolean("firstTimeTutorialsDone", false)
    //             putBoolean("e1", false)
    //             putBoolean("e2", false)
    //             putBoolean("e3", false)
    //             putBoolean("e4", false)
    //             putBoolean("e5", false)
    //             putBoolean("e6", false)
    //             putBoolean("e7", false)
    //             putBoolean("e8", false)
    //         }.apply()
    //         return mutableMapOf(
    //                 "e1" to false,
    //                 "e2" to false,
    //                 "e3" to false,
    //                 "e4" to false,
    //                 "e5" to false,
    //                 "e6" to false,
    //                 "e7" to false,
    //                 "e8" to false,
    //             )
    //     } else {
    //         return mutableMapOf(
    //                 "e1" to sharedPreferences.getBoolean("e1", false),
    //                 "e2" to sharedPreferences.getBoolean("e2", false),
    //                 "e3" to sharedPreferences.getBoolean("e3", false),
    //                 "e4" to sharedPreferences.getBoolean("e4", false),
    //                 "e5" to sharedPreferences.getBoolean("e5", false),
    //                 "e6" to sharedPreferences.getBoolean("e6", false),
    //                 "e7" to sharedPreferences.getBoolean("e7", false),
    //                 "e8" to sharedPreferences.getBoolean("e8", false),
    //         )
    //     }
    // }

    // fun updateTutorialsDone(tutorialNumber: Int) {
    //     val editor = sharedPreferences.edit()
    //     editor.putBoolean("e$tutorialNumber", true)
    //     editor.apply()
    // }

    // fun resetTutorials() {
    //     sharedPreferences.edit().apply {
    //         putBoolean("e1", false)
    //         putBoolean("e2", false)
    //         putBoolean("e3", false)
    //         putBoolean("e4", false)
    //         putBoolean("e5", false)
    //         putBoolean("e6", false)
    //         putBoolean("e7", false)
    //         putBoolean("e8", false)
    //     }.apply()
    //     resetTutorial = true
    // }

    // fun checkResetTutorial(): Boolean {
    //     return if (resetTutorial) {
    //         resetTutorial = false
    //         true
    //     } else
    //         false
    // }
}
