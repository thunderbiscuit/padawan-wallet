/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain.bitcoin

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
import org.bitcoindevkit.SqliteDbConfiguration
import org.bitcoindevkit.TransactionDetails
import org.bitcoindevkit.TxBuilder
import org.bitcoindevkit.TxBuilderResult
import org.bitcoindevkit.WordCount

private const val TAG = "Wallet"
private const val SIGNET_ELECTRUM_URL: String = "ssl://mempool.space:60602"

object Wallet {
    private lateinit var wallet: org.bitcoindevkit.Wallet
    private lateinit var path: String

    private val blockchain: Blockchain by lazy {
        val blockchainConfig = BlockchainConfig.Electrum(ElectrumConfig(SIGNET_ELECTRUM_URL, null, 5u, null, 10u, true))
        val blockchain = Blockchain(blockchainConfig)
        blockchain
    }

    // Setting the path requires the application context and is done once by PadawanWalletApplication
    fun setPath(path: String) {
        Wallet.path = path
    }

    private fun initialize(
        descriptor: Descriptor,
        changeDescriptor: Descriptor,
    ) {
        val database = DatabaseConfig.Sqlite(SqliteDbConfiguration("$path/bdk-sqlite"))
        wallet = org.bitcoindevkit.Wallet(
            descriptor,
            changeDescriptor,
            Network.SIGNET,
            database
        )
    }

    fun loadExistingWallet() {
        val initialWalletData: RequiredInitialWalletData = WalletRepository.getInitialWalletData()
        Log.i(TAG, "Loading existing wallet with descriptor: ${initialWalletData.descriptor}"
        )
        Log.i(TAG, "Loading existing wallet with change descriptor: ${initialWalletData.changeDescriptor}"
        )
        initialize(
            descriptor = Descriptor(initialWalletData.descriptor, Network.SIGNET),
            changeDescriptor = Descriptor(initialWalletData.changeDescriptor, Network.SIGNET),
        )
    }

    fun recoverWallet(recoveryPhrase: String) {
        val mnemonic = Mnemonic.fromString(recoveryPhrase)
        val bip32ExtendedRootKey = DescriptorSecretKey(Network.SIGNET, mnemonic, null)
        val descriptor: Descriptor =
            Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.EXTERNAL, Network.SIGNET)
        val changeDescriptor: Descriptor =
            Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.INTERNAL, Network.SIGNET)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        WalletRepository.saveWallet(
            path,
            descriptor.asStringPrivate(),
            changeDescriptor.asStringPrivate()
        )
        WalletRepository.saveMnemonic(mnemonic.asString())
    }

    fun createWallet() {
        val mnemonic = Mnemonic(WordCount.WORDS12)
        val bip32ExtendedRootKey = DescriptorSecretKey(Network.SIGNET, mnemonic, null)
        val descriptor: Descriptor =
            Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.EXTERNAL, Network.SIGNET)
        val changeDescriptor: Descriptor =
            Descriptor.newBip84(bip32ExtendedRootKey, KeychainKind.INTERNAL, Network.SIGNET)
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        WalletRepository.saveWallet(
            path,
            descriptor.asStringPrivate(),
            changeDescriptor.asStringPrivate()
        )
        WalletRepository.saveMnemonic(mnemonic.asString())
    }

    fun sync() {
        wallet.sync(blockchain = blockchain, progress = null)
    }

    fun getBalance(): ULong {
        return wallet.getBalance().total
    }

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
}
