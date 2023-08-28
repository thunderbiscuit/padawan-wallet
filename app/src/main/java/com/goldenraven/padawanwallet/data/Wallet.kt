/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data

import android.util.Log
import com.goldenraven.padawanwallet.utils.RequiredInitialWalletData
import org.bitcoindevkit.Address
import org.bitcoindevkit.AddressIndex
import org.bitcoindevkit.AddressInfo
import org.bitcoindevkit.Blockchain
import org.bitcoindevkit.BlockchainConfig
import org.bitcoindevkit.DatabaseConfig
import org.bitcoindevkit.Descriptor
import org.bitcoindevkit.DescriptorSecretKey
import org.bitcoindevkit.ElectrumConfig
import org.bitcoindevkit.KeychainKind
import org.bitcoindevkit.Mnemonic
import org.bitcoindevkit.Network
import org.bitcoindevkit.PartiallySignedTransaction
import org.bitcoindevkit.Progress
import org.bitcoindevkit.SqliteDbConfiguration
import org.bitcoindevkit.TransactionDetails
import org.bitcoindevkit.TxBuilder
import org.bitcoindevkit.TxBuilderResult
import org.bitcoindevkit.WordCount
import org.bitcoindevkit.Wallet as BdkWallet

private const val TAG = "Wallet"

object Wallet {

    private const val electrumURL: String = "ssl://electrum.blockstream.info:60002"
    private lateinit var wallet: BdkWallet
    private lateinit var path: String
    private lateinit var blockchain: Blockchain

    object LogProgress: Progress {
        override fun update(progress: Float, message: String?) {
            Log.d(TAG, "updating wallet $progress $message")
        }
    }

    // setting the path requires the application context and is done once by PadawanWalletApplication
    fun setPath(path: String) {
        Wallet.path = path
    }

    private fun initialize(
        descriptor: Descriptor,
        changeDescriptor: Descriptor,
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
        val blockchainConfig = BlockchainConfig.Electrum(ElectrumConfig(electrumURL, null, 5u, null, 10u, true))
        blockchain = Blockchain(blockchainConfig)
    }

    fun loadExistingWallet() {
        val initialWalletData: RequiredInitialWalletData = WalletRepository.getInitialWalletData()
        Log.i(TAG, "Loading existing wallet with descriptor: ${initialWalletData.descriptor}")
        Log.i(TAG, "Loading existing wallet with change descriptor: ${initialWalletData.changeDescriptor}")
        initialize(
            descriptor = Descriptor(initialWalletData.descriptor, Network.TESTNET),
            changeDescriptor = Descriptor(initialWalletData.changeDescriptor, Network.TESTNET),
        )
    }

    fun recoverWallet(recoveryPhrase: String) {
        val mnemonic = Mnemonic.fromString(recoveryPhrase)
        val bip32ExtendedRootKey = DescriptorSecretKey(Network.TESTNET, mnemonic, null)
        val descriptor: Descriptor = Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.EXTERNAL, Network.TESTNET)
        val changeDescriptor: Descriptor = Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.INTERNAL, Network.TESTNET)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        WalletRepository.saveWallet(path, descriptor.asStringPrivate(), changeDescriptor.asStringPrivate())
        WalletRepository.saveMnemonic(mnemonic.asString())
    }

    fun createWallet() {
        val mnemonic = Mnemonic(WordCount.WORDS12)
        val bip32ExtendedRootKey = DescriptorSecretKey(Network.TESTNET, mnemonic, null)
        val descriptor: Descriptor = Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.EXTERNAL, Network.TESTNET)
        val changeDescriptor: Descriptor = Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.INTERNAL, Network.TESTNET)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        WalletRepository.saveWallet(path, descriptor.asStringPrivate(), changeDescriptor.asStringPrivate())
        WalletRepository.saveMnemonic(mnemonic.asString())
    }

    // private fun generateExtendedKey(): ExtendedKeyInfo {
    //     return generateExtendedKey(Network.TESTNET, WordCount.WORDS12, null)
    // }

    // private fun restoreExtendedKeyFromMnemonic(mnemonic: String): ExtendedKeyInfo {
    //     return restoreExtendedKey(Network.TESTNET, mnemonic, null)
    // }

    // private fun createDescriptor(keys: ExtendedKeyInfo): String {
    //     Log.i(TAG,"Descriptor for receive addresses is wpkh(${keys.xprv}/84'/1'/0'/0/*)")
    //     return ("wpkh(${keys.xprv}/84'/1'/0'/0/*)")
    // }

    // private fun createChangeDescriptor(keys: ExtendedKeyInfo): String {
    //     Log.i(TAG, "Descriptor for change addresses is wpkh(${keys.xprv}/84'/1'/0'/1/*)")
    //     return ("wpkh(${keys.xprv}/84'/1'/0'/1/*)")
    // }

    fun sync() {
        wallet.sync(blockchain = blockchain, progress = LogProgress)
    }

    fun getBalance(): ULong {
        return wallet.getBalance().total
    }

    // fun getNewAddress(): AddressInfo {
    //     return wallet.getAddress(AddressIndex.NEW)
    // }

    fun getLastUnusedAddress(): AddressInfo {
        return wallet.getAddress(AddressIndex.LAST_UNUSED)
    }

    fun createTransaction(recipientAddress: String, amount: ULong, feeRate: Float): TxBuilderResult {
        val recipientScriptPubKey = Address(recipientAddress).scriptPubkey()
        return TxBuilder()
            .addRecipient(recipientScriptPubKey, amount)
            .feeRate(satPerVbyte = feeRate)
            .finish(wallet)
    }

    // fun createSendAllTransaction(recipientAddress: String, feeRate: Float): TxBuilderResult {
    //     val recipientScriptPubKey = Address(recipientAddress).scriptPubkey()
    //     return TxBuilder()
    //         .drainWallet()
    //         .drainTo(recipientScriptPubKey)
    //         .feeRate(satPerVbyte = feeRate)
    //         .finish(wallet)
    // }

    fun listTransactions(): List<TransactionDetails> {
        return wallet.listTransactions()
    }

    fun getTransaction(txid: String): TransactionDetails? {
        val allTransactions = listTransactions()
        allTransactions.forEach {
            if (it.txid == txid) {
                return it
            }
        }
        return null
    }

    fun sign(psbt: PartiallySignedTransaction) {
        wallet.sign(psbt)
    }

    fun broadcast(signedPsbt: PartiallySignedTransaction): String {
        blockchain.broadcast(signedPsbt)
        return signedPsbt.txid()
    }

    fun blockchainIsInitialized() = ::blockchain.isInitialized
}
