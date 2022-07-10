/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data

import android.util.Log
import com.goldenraven.padawanwallet.utils.RequiredInitialWalletData
import org.bitcoindevkit.*
import org.bitcoindevkit.Wallet as BdkWallet

private const val TAG = "Wallet"

object Wallet {

    private const val name: String = "padawan-testnet-0"
    private const val electrumURL: String = "ssl://electrum.blockstream.info:60002"
    private lateinit var wallet: BdkWallet
    private lateinit var path: String
    private lateinit var blockchain: Blockchain

    object LogProgress: Progress {
        override fun update(progress: Float, message: String?) {
            Log.d(TAG, "updating wallet $progress $message")
        }
    }

    fun getWallet(): BdkWallet = wallet

    // setting the path requires the application context and is done once by PadawanWalletApplication
    fun setPath(path: String) {
        Wallet.path = path
    }

    private fun initialize(
        descriptor: String,
        changeDescriptor: String,
    ) {
        val database = DatabaseConfig.Sqlite(SqliteDbConfiguration("$path/bdk-sqlite"))
        wallet = BdkWallet(
            descriptor,
            changeDescriptor,
            Network.TESTNET,
            database
        )
    }

    fun createBlockchain() {
        val blockchainConfig = BlockchainConfig.Electrum(ElectrumConfig(electrumURL, null, 5u, null, 10u))
        blockchain = Blockchain(blockchainConfig)
    }

    fun loadExistingWallet() {
        val initialWalletData: RequiredInitialWalletData = Repository.getInitialWalletData()
        Log.i(TAG, "Loading existing wallet with descriptor: ${initialWalletData.descriptor}")
        Log.i(TAG, "Loading existing wallet with change descriptor: ${initialWalletData.changeDescriptor}")
        initialize(
            descriptor = initialWalletData.descriptor,
            changeDescriptor = initialWalletData.changeDescriptor,
        )
    }

    fun recoverWallet(mnemonic: String) {
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

    fun createWallet() {
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
        return generateExtendedKey(Network.TESTNET, WordCount.WORDS12, null)
    }

    private fun restoreExtendedKeyFromMnemonic(mnemonic: String): ExtendedKeyInfo {
        return restoreExtendedKey(Network.TESTNET, mnemonic, null)
    }

    private fun createDescriptor(keys: ExtendedKeyInfo): String {
        Log.i(TAG,"Descriptor for receive addresses is wpkh(${keys.xprv}/84'/1'/0'/0/*)")
        return ("wpkh(${keys.xprv}/84'/1'/0'/0/*)")
    }

    private fun createChangeDescriptor(keys: ExtendedKeyInfo): String {
        Log.i(TAG, "Descriptor for change addresses is wpkh(${keys.xprv}/84'/1'/0'/1/*)")
        return ("wpkh(${keys.xprv}/84'/1'/0'/1/*)")
    }

    fun sync() {
        wallet.sync(blockchain = blockchain, progress = LogProgress)
    }

    fun getBalance(): ULong {
        return wallet.getBalance()
    }

    fun getNewAddress(): AddressInfo {
        return wallet.getAddress(AddressIndex.NEW)
    }

    fun getLastUnusedAddress(): AddressInfo {
        return wallet.getAddress(AddressIndex.LAST_UNUSED)
    }

    fun createTransaction(recipient: String, amount: ULong, feeRate: Float): PartiallySignedBitcoinTransaction {
        return TxBuilder()
            .addRecipient(recipient, amount)
            .feeRate(satPerVbyte = feeRate)
            .finish(wallet)
    }

    fun listTransactions(): List<Transaction> {
        return wallet.getTransactions()
    }

    fun sign(psbt: PartiallySignedBitcoinTransaction) {
        wallet.sign(psbt)
    }

    fun broadcast(signedPsbt: PartiallySignedBitcoinTransaction): String {
        blockchain.broadcast(signedPsbt)
        return signedPsbt.txid()
    }

    fun isBlockChainCreated() = ::blockchain.isInitialized
}
