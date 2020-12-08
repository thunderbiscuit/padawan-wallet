/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet

// import android.content.SharedPreferences
// import android.net.Network
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.bitcoindevkit.bdkjni.Lib
import org.bitcoindevkit.bdkjni.Types.ExtendedKeys
import org.bitcoindevkit.bdkjni.Types.Network
import org.bitcoindevkit.bdkjni.Types.WalletConstructor
import org.bitcoindevkit.bdkjni.Types.WalletPtr
import timber.log.Timber

class PadawanWalletApplication : Application() {

    private lateinit var lib: Lib
    private lateinit var walletPtr: WalletPtr
    private lateinit var name: String
    private lateinit var network: String
    private lateinit var path: String
    private lateinit var descriptor: String
    private lateinit var changeDescriptor: String
    private lateinit var electrumURL: String

    override fun onCreate() {
        super.onCreate()

        // logs
        Timber.plant(Timber.DebugTree())

        // bitcoindevkit
        Lib.load()
        this.lib = Lib()
        setDefaults()
    }

    override fun onTerminate() {
        super.onTerminate()
        lib.destructor(walletPtr)
    }

    private fun setDefaults() {
        this.name = "Padawan Testnet 0"
        this.network = "testnet"
        this.path = applicationContext.filesDir.toString()
        this.electrumURL = "tcp://testet.aranguren.org:51001"
    }

    fun initialize(
        name: String,
        network: String,
        path: String,
        descriptor: String,
        changeDescriptor: String,
        electrumURL: String,
        electrumProxy: String?,
    ) {
        this.name = name
        this.network = network
        this.path = path
        this.descriptor = descriptor
        this.changeDescriptor = changeDescriptor
        this.electrumURL = electrumURL

        walletPtr = lib.constructor(
            WalletConstructor(
                name,
                Network.testnet,
                path,
                descriptor,
                changeDescriptor,
                electrumURL,
                electrumProxy
            )
        )
    }

    fun createWallet(descriptor: String, changeDescriptor: String) {
        setDefaults()
        initialize(
            name,
            network,
            path,
            descriptor,
            changeDescriptor,
            electrumURL,
            electrumProxy = null,
        )
        saveWallet()
    }

    // save wallet parameters so that the wallet can be reloaded upon starting the app
    private fun saveWallet() {
        val editor: SharedPreferences.Editor = getSharedPreferences("current_wallet", Context.MODE_PRIVATE).edit()
        editor.putBoolean("initialized", true)
        editor.putString("name", name)
        editor.putString("network", network)
        editor.putString("path", path)
        editor.putString("descriptor", descriptor)
        editor.putString("changeDescriptor", changeDescriptor)
        editor.putString("electrum_url", electrumURL)
        editor.apply()
    }


    public fun generateExtendedKey(mnemonicWordCount: Int): ExtendedKeys {
        Timber.i("Extended keys generated")
        return lib.generate_extended_key(Network.testnet, mnemonicWordCount)
    }

    public fun createDescriptor(keys: ExtendedKeys): String {
        return ("wpkh(" + keys.ext_priv_key + "/0/*)")
    }

    public fun createChangeDescriptor(keys: ExtendedKeys): String {
        return ("wpkh(" + keys.ext_priv_key + "/1/*)")
    }
}
