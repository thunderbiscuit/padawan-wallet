/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain.bitcoin

import android.util.Log
import com.goldenraven.padawanwallet.utils.RequiredInitialWalletData
import com.goldenraven.padawanwallet.utils.TxType
import com.goldenraven.padawanwallet.utils.txType
import org.bitcoindevkit.Address
import org.bitcoindevkit.AddressInfo
import org.bitcoindevkit.ChainPosition as BdkChainPosition
import org.bitcoindevkit.Amount
import org.bitcoindevkit.Descriptor
import org.bitcoindevkit.DescriptorSecretKey
import org.bitcoindevkit.ElectrumClient
import org.bitcoindevkit.FeeRate
import org.bitcoindevkit.KeychainKind
import org.bitcoindevkit.Mnemonic
import org.bitcoindevkit.Network
import org.bitcoindevkit.Psbt
import org.bitcoindevkit.SqliteStore
import org.bitcoindevkit.Transaction
import org.bitcoindevkit.TxBuilder
import org.bitcoindevkit.Update
import org.bitcoindevkit.WordCount

private const val TAG = "Wallet"
private const val SIGNET_ELECTRUM_URL: String = "ssl://mempool.space:60602"

object Wallet {
    private lateinit var wallet: org.bitcoindevkit.Wallet
    private lateinit var dbPath: String
    private lateinit var db: SqliteStore

    private val blockchainClient: ElectrumClient by lazy { ElectrumClient(SIGNET_ELECTRUM_URL) }

    // Setting the path requires the application context and is done once by PadawanWalletApplication
    fun setPathAndConnectDb(path: String) {
        dbPath = "$path/padawanDB.sqlite"
        db = SqliteStore(dbPath)
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
            dbPath,
            descriptor.toStringWithSecret(),
            changeDescriptor.toStringWithSecret()
        )
        WalletRepository.saveMnemonic(mnemonic.toString())
    }

    private fun initialize(
        descriptor: Descriptor,
        changeDescriptor: Descriptor,
    ) {
        wallet = org.bitcoindevkit.Wallet(
            descriptor,
            changeDescriptor,
            Network.SIGNET,
        )
    }

    fun loadWallet() {
        val initialWalletData: RequiredInitialWalletData = WalletRepository.getInitialWalletData()
        Log.i(TAG, "Loading existing wallet with descriptor: ${initialWalletData.descriptor}")
        Log.i(TAG, "Loading existing wallet with change descriptor: ${initialWalletData.changeDescriptor}")
        val descriptor = Descriptor(initialWalletData.descriptor, Network.SIGNET)
        val changeDescriptor = Descriptor(initialWalletData.changeDescriptor, Network.SIGNET)
        val changeSet = db.read()

        wallet = org.bitcoindevkit.Wallet.newOrLoad(
            descriptor,
            changeDescriptor,
            changeSet,
            Network.SIGNET,
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
            dbPath,
            descriptor.toStringWithSecret(),
            changeDescriptor.toStringWithSecret()
        )
        WalletRepository.saveMnemonic(mnemonic.toString())
    }

    fun fullScan() {
        val fullScanRequest = wallet.startFullScan()
        val update: Update = blockchainClient.fullScan(
            fullScanRequest = fullScanRequest,
            stopGap = 20u,
            batchSize = 10u,
            fetchPrevTxouts = true
        )
        wallet.applyUpdate(update)
        val changeset = wallet.takeStaged()
        if (changeset != null) db.write(changeset)
    }

    fun sync() {
        val syncRequest = wallet.startSyncWithRevealedSpks()
        val update = blockchainClient.sync(
            syncRequest = syncRequest,
            batchSize = 10u,
            fetchPrevTxouts = true
        )
        wallet.applyUpdate(update)
        val changeset = wallet.takeStaged()
        if (changeset != null) db.write(changeset)
    }

    fun getBalance(): ULong {
        return wallet.balance().total.toSat()
    }

    fun getLastUnusedAddress(): AddressInfo {
        return wallet.revealNextAddress(KeychainKind.EXTERNAL)
    }

    fun createPsbt(recipientAddress: String, amount: Amount, feeRate: FeeRate): Psbt {
        val recipientScriptPubKey = Address(recipientAddress, Network.SIGNET).scriptPubkey()
        return TxBuilder()
            .addRecipient(recipientScriptPubKey, amount)
            .feeRate(feeRate)
            .finish(wallet)
    }

    fun sign(psbt: Psbt) {
        wallet.sign(psbt)
    }
    
    fun listTransactions(): List<TransactionDetails> {
        val transactions = wallet.transactions()
        return transactions.map { tx ->
            val (sent, received) = wallet.sentAndReceived(tx.transaction)
            val fee = wallet.calculateFee(tx.transaction)
            val feeRate = wallet.calculateFeeRate(tx.transaction)
            val txType: TxType = txType(sent = sent.toSat(), received = received.toSat())
            val chainPosition: ChainPosition = when (val position = tx.chainPosition) {
                is BdkChainPosition.Unconfirmed -> ChainPosition.Unconfirmed
                is BdkChainPosition.Confirmed -> ChainPosition.Confirmed(position.height, position.timestamp)
            }

            TransactionDetails(
                txid = tx.transaction.computeTxid(),
                sent = sent,
                received = received,
                fee = fee,
                feeRate = feeRate,
                txType = txType,
                chainPosition = chainPosition
            )
        }
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

    fun broadcast(tx: Transaction): String {
        blockchainClient.broadcast(tx)
        return tx.computeTxid()
    }
}
