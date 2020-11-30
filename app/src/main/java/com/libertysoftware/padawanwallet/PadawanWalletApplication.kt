/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet

// import android.content.SharedPreferences
// import android.net.Network
import android.app.Application
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
    private lateinit var network: Network
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
        name = "Padawan Testnet 0"
        network = Network.testnet
        path = applicationContext.filesDir.toString()
        electrumURL = "tcp://testet.aranguren.org:51001"
    }

    fun initialize(
        name: String,
        network: Network,
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
                network,
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
        // saveWalletPrefs()
    }

    public fun generateExtendedKey(mnemonicWordCount: Int): ExtendedKeys {
        Timber.i("Extended keys generated")
        return lib.generate_extended_key(Network.testnet, mnemonicWordCount)
    }
}
