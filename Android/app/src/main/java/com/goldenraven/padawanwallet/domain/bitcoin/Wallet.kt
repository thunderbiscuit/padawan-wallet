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
import org.bitcoindevkit.Connection
import org.bitcoindevkit.ChainPosition as BdkChainPosition
import org.rustbitcoin.bitcoin.Amount
import org.bitcoindevkit.Descriptor
import org.bitcoindevkit.DescriptorSecretKey
import org.bitcoindevkit.ElectrumClient
import org.rustbitcoin.bitcoin.FeeRate
import org.bitcoindevkit.KeychainKind
import org.bitcoindevkit.Mnemonic
import org.rustbitcoin.bitcoin.Network
import org.bitcoindevkit.Psbt
import org.bitcoindevkit.Transaction
import org.bitcoindevkit.TxBuilder
import org.bitcoindevkit.Update
import org.bitcoindevkit.WordCount

private const val TAG = "WalletObject"
private const val SIGNET_ELECTRUM_URL: String = "ssl://mempool.space:60602"

object Wallet {
    private lateinit var wallet: org.bitcoindevkit.Wallet
    private lateinit var dbPath: String
    private lateinit var dbConnection: Connection

    private val blockchainClient: ElectrumClient by lazy { ElectrumClient(SIGNET_ELECTRUM_URL) }
    private var fullScanRequired: Boolean = !WalletRepository.isFullScanCompleted()

    // Setting the path requires the application context and is done once by PadawanWalletApplication
    fun setPathAndConnectDb(path: String) {
        dbPath = "$path/padawanDB_v1.sqlite"
        dbConnection = Connection(dbPath)
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
            dbConnection
        )
    }

    fun loadWallet() {
        val initialWalletData: RequiredInitialWalletData = WalletRepository.getInitialWalletData()
        Log.i(TAG, "Loading existing wallet with descriptor: ${initialWalletData.descriptor}")
        Log.i(TAG, "Loading existing wallet with change descriptor: ${initialWalletData.changeDescriptor}")
        val descriptor = Descriptor(initialWalletData.descriptor, Network.SIGNET)
        val changeDescriptor = Descriptor(initialWalletData.changeDescriptor, Network.SIGNET)

        wallet = org.bitcoindevkit.Wallet.load(
            descriptor,
            changeDescriptor,
            dbConnection,
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

    private fun fullScan() {
        val fullScanRequest = wallet.startFullScan().build()
        val update: Update = blockchainClient.fullScan(
            fullScanRequest = fullScanRequest,
            stopGap = 20u,
            batchSize = 10u,
            fetchPrevTxouts = true
        )
        wallet.applyUpdate(update)
        wallet.persist(dbConnection)
    }

    fun sync() {
        if (fullScanRequired) {
            Log.i(TAG, "Full scan required")
            fullScan()
            WalletRepository.fullScanCompleted()
            fullScanRequired = false
        } else {
            Log.i(TAG, "Just a normal sync!")
            val syncRequest = wallet.startSyncWithRevealedSpks().build()
            val update = blockchainClient.sync(
                syncRequest = syncRequest,
                batchSize = 10u,
                fetchPrevTxouts = true
            )
            wallet.applyUpdate(update)
            wallet.persist(dbConnection)
        }
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
                is BdkChainPosition.Confirmed -> ChainPosition.Confirmed(
                    position.confirmationBlockTime.blockId.height,
                    position.confirmationBlockTime.confirmationTime
                )
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