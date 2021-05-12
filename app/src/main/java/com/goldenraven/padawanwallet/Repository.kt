/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import android.content.SharedPreferences
import com.goldenraven.padawanwallet.utils.RequiredInitialWalletData
import timber.log.Timber

object Repository {

    private lateinit var sharedPreferences: SharedPreferences
    public fun setSharedPreferences(sharedPref: SharedPreferences) {
        this.sharedPreferences = sharedPref
    }

    public fun doesWalletExist(): Boolean {
        // val currentWallet: SharedPreferences = applicationContext.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
        val currentWalletExists: Boolean = sharedPreferences.getBoolean("initialized", false)
        Timber.i("[PADAWANLOGS] Value of currentWalletExists at launch: $currentWalletExists")
        return currentWalletExists
    }

    public fun getInitialWalletData(): RequiredInitialWalletData {
        // val currentWallet: SharedPreferences = applicationContext.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
        val descriptor: String = sharedPreferences.getString("descriptor", null)!!
        val changeDescriptor: String = sharedPreferences.getString("changeDescriptor", null)!!
        return RequiredInitialWalletData(descriptor, changeDescriptor)
    }

    public fun saveWallet(path: String, descriptor: String, changeDescriptor: String) {
        Timber.i("[PADAWANLOGS] Saved wallet: path -> $path, descriptor -> $descriptor, change descriptor -> $changeDescriptor")
        val editor = sharedPreferences.edit()
        editor.putBoolean("initialized", true)
        editor.putString("path", path)
        editor.putString("descriptor", descriptor)
        editor.putString("changeDescriptor", changeDescriptor)
        editor.apply()
    }

    public fun saveMnemonic(mnemonic: String) {
        Timber.i("[PADAWANLOGS] The seed phrase is: $mnemonic")
        val editor = sharedPreferences.edit()
        editor.putString("mnemonic", mnemonic)
        editor.apply()
    }

    public fun getMnemonic(): String {
        return sharedPreferences.getString("mnemonic", "No seed phrase saved") ?: "Seed phrase not there"
    }

    public fun wasOneTimeFaucetCallDone(): Boolean {
        return sharedPreferences.getBoolean("oneTimeFaucetCallDone", false)
    }

    public fun oneTimeFaucetCallDone(): Unit {
        val editor = sharedPreferences.edit()
        editor.putBoolean("oneTimeFaucetCallDone", true)
        editor.apply()
    }

    public fun offerFaucetCallDone(): Unit {
        val editor = sharedPreferences.edit()
        editor.putBoolean("offerFaucetCallDone", true)
        editor.apply()
    }

    public fun loadTutorialsDone(): MutableMap<String, Boolean> {
        val firstTimeTutorialsDone: Boolean = sharedPreferences.getBoolean("firstTimeTutorialsDone", true)
        val editor = sharedPreferences.edit()

        if (firstTimeTutorialsDone) {
            editor.putBoolean("firstTimeTutorialsDone", false)
            editor.putBoolean("e1", false)
                .putBoolean("e2", false)
                .putBoolean("e3", false)
                .putBoolean("e4", false)
                .putBoolean("e5", false)
                .putBoolean("e6", false)
                .putBoolean("e7", false)
                .putBoolean("e8", false)
                .apply()
            return mutableMapOf(
                    "e1" to false,
                    "e2" to false,
                    "e3" to false,
                    "e4" to false,
                    "e5" to false,
                    "e6" to false,
                    "e7" to false,
                    "e8" to false,
                )
        } else {
            return mutableMapOf(
                    "e1" to sharedPreferences.getBoolean("e1", false),
                    "e2" to sharedPreferences.getBoolean("e2", false),
                    "e3" to sharedPreferences.getBoolean("e3", false),
                    "e4" to sharedPreferences.getBoolean("e4", false),
                    "e5" to sharedPreferences.getBoolean("e5", false),
                    "e6" to sharedPreferences.getBoolean("e6", false),
                    "e7" to sharedPreferences.getBoolean("e7", false),
                    "e8" to sharedPreferences.getBoolean("e8", false),
            )
        }
    }

    public fun updateTutorialsDone(tutorialNumber: Int): Unit {
        val editor = sharedPreferences.edit()
        editor.putBoolean("e$tutorialNumber", true)
        editor.apply()
    }

    public fun resetTutorials(): Unit {
        val editor = sharedPreferences.edit()
        editor.putBoolean("e1", false)
            .putBoolean("e2", false)
            .putBoolean("e3", false)
            .putBoolean("e4", false)
            .putBoolean("e5", false)
            .putBoolean("e6", false)
            .putBoolean("e7", false)
            .putBoolean("e8", false)
            .apply()

    }
}
