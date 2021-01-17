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
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.PadawanWalletApplication
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentWalletBroadcastBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.bitcoindevkit.bdkjni.Types.CreateTxResponse
import org.bitcoindevkit.bdkjni.Types.RawTransaction
import org.bitcoindevkit.bdkjni.Types.SignResponse
import org.bitcoindevkit.bdkjni.Types.Txid
import timber.log.Timber

class WalletBroadcast : Fragment() {

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
            // verifyTransaction()
            broadcastTransaction()
            // TimeUnit.SECONDS.sleep(4)
            navController.navigate(R.id.action_walletVerify_to_walletHome)
        }

        binding.buttonBackTransactionBuilder.setOnClickListener {
            navController.navigate(R.id.action_walletVerify_to_walletSend)
        }
    }

    private fun verifyTransaction() {
        val app = requireActivity().application as PadawanWalletApplication
        val addresseesAndAmounts: List<Pair<String, String>> = listOf(Pair(address, amount))
        val feeRate = 1F

        Timber.i("[PADAWANLOGS] Send addresses are: $addresseesAndAmounts")

        try {
            transactionDetails = app.createTransaction(feeRate, addresseesAndAmounts, false, null, null, null)
            // Timber.i("[PADAWANLOGS] transactionDetails from WalletVerify fragment is: $transactionDetails")
        } catch (e: Throwable) {
            Timber.i("[PADAWANLOGS] Verify transaction failed: ${e.message}")
            // TODO: Show toast related to error
            return
        }
    }

    private fun broadcastTransaction() {
        val app = requireActivity().application as PadawanWalletApplication
        var txidString: String = "string of txid"

        try {
            val signResponse: SignResponse = app.sign(transactionDetails.psbt)
            val rawTx: RawTransaction = app.extractPsbt(signResponse.psbt)
            val txid: Txid = app.broadcast(rawTx.transaction)
            txidString = txid.toString()
            Timber.i("[PADAWANLOGS] Transaction was broadcast! txid: $txid, txidString: $txidString")
        } catch (e: Throwable) {
            Timber.i("[PADAWANLOGS] ${e.message}")
        }
        // showBroadcastSuccessToast(txid = txidString)
        showBroadcastSuccessSnackbar(txid = txidString)
    }

    private fun showBroadcastSuccessSnackbar(txid: String) {
        val broadcastSuccessSnackbar = Snackbar.make(requireView(), "Transaction was broadcast successfully!", Snackbar.LENGTH_LONG)
        broadcastSuccessSnackbar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
        broadcastSuccessSnackbar.view.background = resources.getDrawable(R.drawable.background_toast, null)
        broadcastSuccessSnackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        broadcastSuccessSnackbar.show()
    }
}
