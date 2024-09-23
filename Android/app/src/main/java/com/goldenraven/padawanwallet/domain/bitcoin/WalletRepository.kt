/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain.bitcoin

import android.content.SharedPreferences
import android.util.Log
import com.goldenraven.padawanwallet.utils.RequiredInitialWalletData
import java.io.File

private const val TAG = "WalletRepository"

object WalletRepository {
    private lateinit var sharedPreferences: SharedPreferences
    private var validDbExists: Boolean = false

    fun setSharedPreferences(sharedPref: SharedPreferences, filesPath: String) {
        sharedPreferences = sharedPref
        checkIfDbExists(filesPath)
    }

    // This method checks if the wallet file version X exists in the given path. It's a bit hacky,
    // but allows us to upgrade the app even if the persistence is incompatible; we simply name the
    // persistence file with a new version name, the wallet will not find this file, and the user
    // will have to recover the wallet, creating a new persistence instead of handling a database
    // migration.
    private fun checkIfDbExists(filesPath: String) {
        val file = File("$filesPath/padawanDB_v2.sqlite")
        validDbExists = file.exists()
        Log.i(TAG, "We checked at $filesPath and the value of validDbExists is $validDbExists")
    }

    private fun walletInitialized(): Boolean {
        return sharedPreferences.getBoolean("initialized", false)
    }

    fun doesWalletExist(): Boolean {
        Log.i(TAG, "Can we go to Root Wallet Screen? -> Valid DB exists: $validDbExists, Wallet is initialized: ${walletInitialized()}")
        return validDbExists && walletInitialized()
    }

    fun getInitialWalletData(): RequiredInitialWalletData {
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

    fun fullScanCompleted() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("fullSyncCompleted", true)
        editor.apply()
    }

    fun isFullScanCompleted(): Boolean {
        return sharedPreferences.getBoolean("fullSyncCompleted", false)
    }
}
