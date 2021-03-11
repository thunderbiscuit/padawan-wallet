/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import android.content.Context
import android.content.SharedPreferences
import timber.log.Timber

object Repository {

//    TODO should I provide context once (say in PadawanWalletApplication) or should context be provided on every method?
//    TODO is it better to pass the application context or the shared preferences editor directly?

//    private lateinit var applicationContext: Context
//    public fun setApplicationContext(applicationContext: Context) {
//        this.applicationContext = applicationContext
//    }

    public fun initialize() {
        Timber.i("[PADAWANLOGS]: Repository is initialized")
    }

    public fun doesWalletExist(applicationContext: Context): Boolean {
        val currentWallet: SharedPreferences = applicationContext.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
        val currentWalletExists: Boolean = currentWallet.getBoolean("initialized", false)
        Timber.i("[PADAWANLOGS] Value of currentWalletExists at launch: $currentWalletExists")
        return currentWalletExists
    }

    public fun getInitialWalletData(applicationContext: Context): RequiredInitialWalletData {
        val currentWallet: SharedPreferences = applicationContext.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
        val descriptor: String = currentWallet.getString("descriptor", null)!!
        val changeDescriptor: String = currentWallet.getString("changeDescriptor", null)!!
        return RequiredInitialWalletData(descriptor, changeDescriptor)
    }

    public fun saveWallet(editor: SharedPreferences.Editor, path: String, descriptor: String, changeDescriptor: String) {
        Timber.i("[PADAWANLOGS] Saved wallet: path -> $path, descriptor -> $descriptor, change descriptor -> $changeDescriptor")
        editor.putBoolean("initialized", true)
        editor.putString("path", path)
        editor.putString("descriptor", descriptor)
        editor.putString("changeDescriptor", changeDescriptor)
        editor.apply()
    }

    public fun saveMnemonic(editor: SharedPreferences.Editor, mnemonic: String) {
        Timber.i("[PADAWANLOGS] The seed phrase is: ${mnemonic}")
        editor.putString("mnemonic", mnemonic)
        editor.apply()
    }
}
