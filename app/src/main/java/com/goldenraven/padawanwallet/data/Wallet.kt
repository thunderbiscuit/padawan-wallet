/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data

import android.util.Log
import com.goldenraven.padawanwallet.utils.RequiredInitialWalletData
import org.bitcoindevkit.*

private const val TAG = "Wallet"

object Wallet {

    private lateinit var wallet: OnlineWallet
    private const val name: String = "padawan-testnet-0"
    private lateinit var path: String
    private const val electrumURL: String = "ssl://electrum.blockstream.info:60002"

    object LogProgress: BdkProgress {
        override fun update(progress: Float, message: String?) {
            Log.d(TAG, "updating wallet $progress $message")
        }
    }

    fun getWallet(): OnlineWallet = wallet

    // setting the path requires the application context and is done once by PadawanWalletApplication
    fun setPath(path: String) {
        Wallet.path = path
    }

    private fun initialize(
        descriptor: String,
        changeDescriptor: String,
    ): Unit {
        val database = DatabaseConfig.Sled(SledDbConfiguration(path, name))
        val blockchain = BlockchainConfig.Electrum(ElectrumConfig(electrumURL, null, 5u, null, 10u))
        wallet = OnlineWallet(
            descriptor,
            changeDescriptor,
            Network.TESTNET,
            database,
            blockchain
        )
    }

    fun loadExistingWallet(): Unit {
        val initialWalletData: RequiredInitialWalletData = Repository.getInitialWalletData()
        Log.i(TAG, "Loading existing wallet with descriptor: ${initialWalletData.descriptor}")
        Log.i(TAG, "Loading existing wallet with change descriptor: ${initialWalletData.changeDescriptor}")
        initialize(
            descriptor = initialWalletData.descriptor,
            changeDescriptor = initialWalletData.changeDescriptor,
        )
    }

    fun recoverWallet(mnemonic: String): Unit {
        val keys = restoreExtendedKeyFromMnemonic(mnemonic)
        val descriptor: String = createDescriptor(keys)
        val changeDescriptor: String = createChangeDescriptor(keys)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        Repository.saveWallet(path, descriptor, changeDescriptor)
        Repository.saveMnemonic(keys.mnemonic)
    }

    fun createWallet(): Unit {
        val keys = generateExtendedKey()
        val descriptor: String = createDescriptor(keys)
        val changeDescriptor: String = createChangeDescriptor(keys)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        Repository.saveWallet(path, descriptor, changeDescriptor)
        Repository.saveMnemonic(keys.mnemonic)
    }

    private fun generateExtendedKey(): ExtendedKeyInfo {
        return generateExtendedKey(Network.TESTNET, MnemonicType.WORDS12, null)
    }

    private fun restoreExtendedKeyFromMnemonic(mnemonic: String): ExtendedKeyInfo {
        return restoreExtendedKey(Network.TESTNET, mnemonic, null)
    }

    private fun createDescriptor(keys: ExtendedKeyInfo): String {
        Log.i(TAG,"Descriptor for receive addresses is wpkh(${keys.xprv}/84'/1'/0'/0/*)")
        return ("wpkh(" + keys.xprv + "/84'/1'/0'/0/*)")
    }

    private fun createChangeDescriptor(keys: ExtendedKeyInfo): String {
        Log.i(TAG, "Descriptor for change addresses is wpkh(${keys.xprv}/84'/1'/0'/1/*)")
        return ("wpkh(" + keys.xprv + "/84'/1'/0'/1/*)")
    }

    fun sync(max_address: UInt?=null) {
        wallet.sync(LogProgress, max_address)
    }

    fun getBalance(): ULong {
        return wallet.getBalance()
    }

    fun getNewAddress(): String {
        return wallet.getNewAddress()
    }

    fun createTransaction(recipient: String, amount: ULong, fee_rate: Float?): PartiallySignedBitcoinTransaction {
        return PartiallySignedBitcoinTransaction(wallet, recipient, amount, fee_rate)
    }

    fun listTransactions(): List<Transaction> {
        return wallet.getTransactions()
    }

    fun sign(psbt: PartiallySignedBitcoinTransaction): Unit {
        wallet.sign(psbt)
    }

    fun broadcast(psbt: PartiallySignedBitcoinTransaction): Transaction {
        return wallet.broadcast(psbt)
    }
}
