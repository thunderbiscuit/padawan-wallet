/*
 * Copyright 2020-2026 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.domain.bitcoin

import android.util.Log
import com.coyotebitcoin.padawanwallet.domain.utils.RequiredInitialWalletData
import com.coyotebitcoin.padawanwallet.domain.utils.netSendWithoutFees
import org.bitcoindevkit.Address
import org.bitcoindevkit.AddressInfo
import org.bitcoindevkit.Amount
import org.bitcoindevkit.ChainPosition as BdkChainPosition
import org.bitcoindevkit.Descriptor
import org.bitcoindevkit.DescriptorSecretKey
import org.bitcoindevkit.ElectrumClient
import org.bitcoindevkit.FeeRate
import org.bitcoindevkit.KeychainKind
import org.bitcoindevkit.Mnemonic
import org.bitcoindevkit.Network
import org.bitcoindevkit.NetworkKind
import org.bitcoindevkit.Persister
import org.bitcoindevkit.Psbt
import org.bitcoindevkit.Transaction
import org.bitcoindevkit.TxBuilder
import org.bitcoindevkit.Txid
import org.bitcoindevkit.Update
import org.bitcoindevkit.Wallet as BdkWallet
import org.bitcoindevkit.WordCount

private const val TAG = "WalletObject"
private const val SIGNET_ELECTRUM_URL: String = "ssl://mempool.space:60602"
const val PERSISTENCE_VERSION = "V2"

object Wallet {
    private lateinit var wallet: BdkWallet
    private lateinit var dbPath: String
    private lateinit var db: Persister

    private val blockchainClient: ElectrumClient by lazy { ElectrumClient(SIGNET_ELECTRUM_URL) }
    private var fullScanRequired: Boolean = !WalletRepository.isFullScanCompleted()

    // Setting the path requires the application context and is done once by PadawanWalletApplication
    fun setPathAndConnectDb(path: String) {
        dbPath = "$path/padawanDB_$PERSISTENCE_VERSION.sqlite3"
        db = Persister.newSqlite(dbPath)
        Log.i(TAG, "Connecting to database at: $dbPath")
    }

    fun createWallet() {
        val mnemonic = Mnemonic(WordCount.WORDS12)
        val bip32ExtendedRootKey = DescriptorSecretKey(NetworkKind.TEST, mnemonic, null)
        val descriptor: Descriptor =
            Descriptor.newBip84(
                bip32ExtendedRootKey,
                KeychainKind.EXTERNAL,
                NetworkKind.TEST,
            )
        val changeDescriptor: Descriptor =
            Descriptor.newBip84(
                bip32ExtendedRootKey,
                KeychainKind.INTERNAL,
                NetworkKind.TEST,
            )
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        WalletRepository.saveWallet(
            dbPath,
            descriptor.toStringWithSecret(),
            changeDescriptor.toStringWithSecret(),
        )
        WalletRepository.saveMnemonic(mnemonic.toString())
    }

    private fun initialize(
        descriptor: Descriptor,
        changeDescriptor: Descriptor,
    ) {
        wallet =
            BdkWallet(
                descriptor,
                changeDescriptor,
                Network.SIGNET,
                db,
            )
    }

    fun loadWallet() {
        val initialWalletData: RequiredInitialWalletData = WalletRepository.getInitialWalletData()
        Log.i(TAG, "Loading existing wallet with descriptor: ${initialWalletData.descriptor}")
        Log.i(TAG, "Loading existing wallet with change descriptor: ${initialWalletData.changeDescriptor}")
        val descriptor = Descriptor(initialWalletData.descriptor, NetworkKind.TEST)
        val changeDescriptor = Descriptor(initialWalletData.changeDescriptor, NetworkKind.TEST)

        wallet =
            BdkWallet.load(
                descriptor,
                changeDescriptor,
                db,
            )
    }

    fun recoverWallet(recoveryPhrase: String) {
        val mnemonic = Mnemonic.fromString(recoveryPhrase)
        val bip32ExtendedRootKey = DescriptorSecretKey(NetworkKind.TEST, mnemonic, null)
        val descriptor: Descriptor =
            Descriptor.newBip84(
                bip32ExtendedRootKey,
                KeychainKind.EXTERNAL,
                NetworkKind.TEST,
            )
        val changeDescriptor: Descriptor =
            Descriptor.newBip84(
                bip32ExtendedRootKey,
                KeychainKind.INTERNAL,
                NetworkKind.TEST,
            )
        initialize(
            descriptor = descriptor,
            changeDescriptor = changeDescriptor,
        )
        WalletRepository.saveWallet(
            dbPath,
            descriptor.toStringWithSecret(),
            changeDescriptor.toStringWithSecret(),
        )
        WalletRepository.saveMnemonic(mnemonic.toString())
    }

    private fun fullScan() {
        val fullScanRequest = wallet.startFullScan().build()
        val update: Update =
            blockchainClient.fullScan(
                request = fullScanRequest,
                stopGap = 20u,
                batchSize = 10u,
                fetchPrevTxouts = true,
            )
        wallet.applyUpdate(update)
        wallet.persist(db)
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
            val update =
                blockchainClient.sync(
                    request = syncRequest,
                    batchSize = 10u,
                    fetchPrevTxouts = true,
                )
            wallet.applyUpdate(update)
            wallet.persist(db)
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
        return TxBuilder().addRecipient(recipientScriptPubKey, amount).feeRate(feeRate).finish(wallet)
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
            val txType: TxType = TxType.fromAmounts(sent, received)
            val paymentAmount =
                if (txType == TxType.OUTBOUND) {
                    netSendWithoutFees(
                        txSatsOut = sent.toSat(),
                        txSatsIn = received.toSat(),
                        fee = fee.toSat(),
                    )
                } else {
                    0uL
                }
            val balanceDelta: Long = received.toSat().toLong() - sent.toSat().toLong()
            val chainPosition: ChainPosition =
                when (val position = tx.chainPosition) {
                    is BdkChainPosition.Unconfirmed -> ChainPosition.Unconfirmed
                    is BdkChainPosition.Confirmed ->
                        ChainPosition.Confirmed(
                            position.confirmationBlockTime.blockId.height,
                            position.confirmationBlockTime.confirmationTime,
                        )
                }

            TransactionDetails(
                txid = tx.transaction.computeTxid(),
                sent = sent,
                received = received,
                paymentAmount = paymentAmount,
                balanceDelta = balanceDelta,
                fee = fee,
                feeRate = feeRate,
                txType = txType,
                chainPosition = chainPosition,
            )
        }
    }

    fun getTransaction(txid: Txid): TransactionDetails? {
        val allTransactions = listTransactions()
        allTransactions.forEach {
            if (it.txid == txid) {
                return it
            }
        }
        return null
    }

    fun broadcast(tx: Transaction): Txid {
        blockchainClient.transactionBroadcast(tx)
        return tx.computeTxid()
    }
}
