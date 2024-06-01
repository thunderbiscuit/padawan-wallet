/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain.bitcoin

import android.content.SharedPreferences
import android.util.Log
import com.goldenraven.padawanwallet.utils.RequiredInitialWalletData

private const val TAG = "WalletRepository"

object WalletRepository {
    private lateinit var sharedPreferences: SharedPreferences
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
        Log.i(
            TAG,
            "Saved wallet: path -> $path, descriptor -> $descriptor, change descriptor -> $changeDescriptor"
        )
        sharedPreferences.edit().apply {
            putBoolean("initialized", true)
            putString("path", path)
            putString("descriptor", descriptor)
            putString("changeDescriptor", changeDescriptor)
        }.apply()
    }

    fun saveMnemonic(mnemonic: String) {
        Log.i(TAG, "The seed phrase is: $mnemonic")
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
}
