/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.Wallet
import com.goldenraven.padawanwallet.data.Tx
import com.goldenraven.padawanwallet.databinding.FragmentWalletBroadcastBinding
import com.goldenraven.padawanwallet.home.HomeViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.bitcoindevkit.bdkjni.Types.CreateTxResponse
import org.bitcoindevkit.bdkjni.Types.RawTransaction
import org.bitcoindevkit.bdkjni.Types.SignResponse
import org.bitcoindevkit.bdkjni.Types.Txid
import timber.log.Timber

class WalletBroadcast : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentWalletBroadcastBinding
    private lateinit var amount: String
    private lateinit var address: String
    private lateinit var transactionDetails: CreateTxResponse

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        amount = requireArguments().getString("amount", "No amount provided")
        address = requireArguments().getString("address", "No address provided")

        binding = FragmentWalletBroadcastBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

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
            showErrorSnackbar(e.message!!)
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
        showBroadcastSuccessSnackbar(txid = txidString)
    }

    private fun addTxToDatabase(txid: String, timestamp: String, valueIn: Int, valueOut: Int, fees: Int) {
        val tx = Tx(txid, timestamp, valueIn, valueOut, fees)
        viewModel.addTx(tx)
    }

    private fun showBroadcastSuccessSnackbar(txid: String) {
        val broadcastSuccessSnackbar = Snackbar.make(requireView(), "Transaction was broadcast successfully!", Snackbar.LENGTH_LONG)
        broadcastSuccessSnackbar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
        broadcastSuccessSnackbar.view.background = resources.getDrawable(R.drawable.background_toast, null)
        broadcastSuccessSnackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        broadcastSuccessSnackbar.show()
    }
    private fun showErrorSnackbar(errorMessage: String) {
        val broadcastSuccessSnackbar = Snackbar.make(requireView(), "Error: $errorMessage", Snackbar.LENGTH_LONG)
        broadcastSuccessSnackbar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
        broadcastSuccessSnackbar.view.background = resources.getDrawable(R.drawable.background_toast_error, null)
        broadcastSuccessSnackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        broadcastSuccessSnackbar.show()
    }
}
