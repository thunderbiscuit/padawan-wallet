/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Tx
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentWalletBroadcastBinding
import com.goldenraven.padawanwallet.utils.SnackbarLevel
import com.goldenraven.padawanwallet.utils.fireSnackbar
import com.goldenraven.padawanwallet.utils.isSend
import com.goldenraven.padawanwallet.utils.netSendWithoutFees
import com.goldenraven.padawanwallet.wallet.WalletViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.bitcoindevkit.bdkjni.Types.CreateTxResponse
import org.bitcoindevkit.bdkjni.Types.RawTransaction
import org.bitcoindevkit.bdkjni.Types.SignResponse
import org.bitcoindevkit.bdkjni.Types.Txid
import timber.log.Timber

class WalletBroadcast : Fragment() {

    private lateinit var viewModel: WalletViewModel
    private lateinit var binding: FragmentWalletBroadcastBinding
    private lateinit var amount: String
    private lateinit var address: String
    private lateinit var transactionDetails: CreateTxResponse

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        amount = requireArguments().getString("amount", "No amount provided")
        address = requireArguments().getString("address", "No address provided")

        binding = FragmentWalletBroadcastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WalletViewModel::class.java)

        verifyTransaction()
        Timber.i("[PADAWANLOGS] transactionDetails from WalletVerify fragment is: $transactionDetails")

        val fees: Long = transactionDetails.details.fees
        val total: String = (amount.toLong() + fees).toString()

        binding.amountTransacted.text = amount
        binding.fees.text = fees.toString()
        binding.total.text = total
        binding.sendToAddress.text = address

        val navController = Navigation.findNavController(view)

        binding.buttonBroadcastTransaction.setOnClickListener {
            broadcastTransaction()
            // TimeUnit.SECONDS.sleep(4)
            navController.navigate(R.id.action_walletVerify_to_walletHome)
        }

        binding.buttonBackTransactionBuilder.setOnClickListener {
            navController.navigate(R.id.action_walletVerify_to_walletSend)
        }
    }

    private fun verifyTransaction() {
        val addresseesAndAmounts: List<Pair<String, String>> = listOf(Pair(address, amount))
        val feeRate = 1F

        Timber.i("[PADAWANLOGS] Send addresses are (from broadcast fragment): $addresseesAndAmounts")

        try {
            transactionDetails = Wallet.createTransaction(feeRate, addresseesAndAmounts, false, null, null, null)
            // Timber.i("[PADAWANLOGS] transactionDetails from WalletVerify fragment is: $transactionDetails")
        } catch (e: Throwable) {
            Timber.i("[PADAWANLOGS] Verify transaction failed: ${e.message}")
            fireSnackbar(
                requireView(),
                SnackbarLevel.ERROR,
                "Error: ${e.message}"
            )
        }
    }

    private fun broadcastTransaction() {
        var txidString: String = "string of txid"

        try {
            val signResponse: SignResponse = Wallet.sign(transactionDetails.psbt)
            val rawTx: RawTransaction = Wallet.extractPsbt(signResponse.psbt)
            val txid: Txid = Wallet.broadcast(rawTx.transaction)
            txidString = txid.toString()

            Timber.i("[PADAWANLOGS] Transaction was broadcast! Data added to database:")
            Timber.i("[PADAWANLOGS] Transaction was broadcast! txid: ${transactionDetails.details.txid}")
            Timber.i("[PADAWANLOGS] Transaction was broadcast! timestamp: ${transactionDetails.details.timestamp}")
            Timber.i("[PADAWANLOGS] Transaction was broadcast! received: ${transactionDetails.details.received.toInt()}")
            Timber.i("[PADAWANLOGS] Transaction was broadcast! sent: ${transactionDetails.details.sent.toInt()}")
            Timber.i("[PADAWANLOGS] Transaction was broadcast! fees: ${transactionDetails.details.fees.toInt()}")
            // add tx to database
            addTxToDatabase(
                transactionDetails.details.txid,
                transactionDetails.details.timestamp.toString(),
                transactionDetails.details.received.toInt(),
                transactionDetails.details.sent.toInt(),
                transactionDetails.details.fees.toInt(),
            )
            Timber.i("[PADAWANLOGS] Transaction was broadcast! txid: $txid, txidString: $txidString")
        } catch (e: Throwable) {
            Timber.i("[PADAWANLOGS] ${e.message}")
        }
        fireSnackbar(
            requireView(),
            SnackbarLevel.INFO,
            "Transaction was broadcast successfully!"
        )
    }

    private fun addTxToDatabase(txid: String, timestamp: String, txSatsIn: Int, txSatsOut: Int, fees: Int) {
        // val isSend: Boolean = isSend(sent = valueOut, received = valueIn)
        // val tx = Tx(txid, timestamp, valueIn, valueOut, fees, isSend)
        val isSend: Boolean = isSend(sent = txSatsOut, received = txSatsIn)
        var valueIn: Int = 0
        var valueOut: Int = 0
        when (isSend) {
            true -> {
                valueOut = netSendWithoutFees(
                    txSatsOut = txSatsOut,
                    txSatsIn = txSatsIn,
                    fees = fees
                )
            }
            false -> {
                valueIn = txSatsIn
            }
        }
        val transaction: Tx = Tx(
            txid = txid,
            date = timestamp,
            valueIn = valueIn,
            valueOut = valueOut,
            fees = fees,
            isSend = isSend
        )
        Timber.i("[PADAWANLOGS] From addTxToDatabase, the tx is $transaction")
        viewModel.addTx(transaction)
    }

    private fun showErrorSnackbar(errorMessage: String) {
        val broadcastSuccessSnackbar = Snackbar.make(requireView(), "Error: $errorMessage", Snackbar.LENGTH_LONG)
        broadcastSuccessSnackbar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
        broadcastSuccessSnackbar.view.background = resources.getDrawable(R.drawable.background_toast_error, null)
        broadcastSuccessSnackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        broadcastSuccessSnackbar.show()
    }
}
