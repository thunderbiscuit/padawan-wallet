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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.PadawanWalletApplication
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentWalletSendBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.bitcoindevkit.bdkjni.Types.CreateTxResponse
import timber.log.Timber

class WalletSend : Fragment() {

    private lateinit var binding: FragmentWalletSendBinding
    private lateinit var transactionDetails: CreateTxResponse
    private lateinit var address: String
    private lateinit var amount: String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletSendBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.button5.setOnClickListener {
            navController.navigate(R.id.action_walletSend_to_walletHome)
        }

        binding.buttonVerify.setOnClickListener {
            verifyTransaction()
            // val bundle = bundleOf("address" to address, "amount" to amount, "transactionDetails" to transactionDetails)
            val bundle = bundleOf("address" to address, "amount" to amount)
            navController.navigate(R.id.action_walletSend_to_walletVerify, bundle)
        }

        binding.feeRate.setOnClickListener {
            showFeatureInDevelopmentSnackbar()
        }
    }

    private fun verifyTransaction() {
        val app = requireActivity().application as PadawanWalletApplication
        address = binding.sendAddress.text.toString().trim()
        amount = binding.sendAmount.text.toString()
        val addresseesAndAmounts: List<Pair<String, String>> = listOf(Pair(address, amount))
        val feeRate = 1F

        Timber.i("[PADAWANLOGS] Send addresses are: $addresseesAndAmounts")

        try {
            transactionDetails = app.createTransaction(feeRate, addresseesAndAmounts, false, null, null, null)
            Timber.i("[PADAWANLOGS] transactionDetails from WalletSend fragment is: $transactionDetails")
        } catch (e: Throwable) {
            Timber.i("[PADAWANLOGS] Verify transaction failed: ${e.message}")
            // TODO: Show toast related to error
            return
        }
    }

    private fun showFeatureInDevelopmentSnackbar() {
        val featureInDevelopmentSnackbar = Snackbar.make(requireView(), "Choosing your fee rate is a feature currently in development!", Snackbar.LENGTH_LONG)
        featureInDevelopmentSnackbar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
        featureInDevelopmentSnackbar.view.background = resources.getDrawable(R.drawable.background_toast, null)
        featureInDevelopmentSnackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        featureInDevelopmentSnackbar.show()
    }
}
