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
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentWalletBuildBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.bitcoindevkit.bdkjni.Types.CreateTxResponse
import timber.log.Timber

class WalletBuild : Fragment() {

    private lateinit var binding: FragmentWalletBuildBinding
    private lateinit var transactionDetails: CreateTxResponse
    private lateinit var address: String
    private lateinit var amount: String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletBuildBinding.inflate(inflater, container, false)
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
            val validInputs: Boolean = verifyTransaction()

            if (validInputs) {
                val bundle = bundleOf("address" to address, "amount" to amount)
                navController.navigate(R.id.action_walletSend_to_walletVerify, bundle)
            } else {
                Timber.i("[PADAWANLOGS] Inputs were not valid")
            }
        }

        binding.feeRate.setOnClickListener {
            showFeatureInDevelopmentSnackbar()
        }
    }

    private fun verifyTransaction(): Boolean {
        address = binding.sendAddress.text.toString().trim()
        amount = binding.sendAmount.text.toString().trim()

        if (address == "") {
            showEmptyField("Address")
            Timber.i("[PADAWANLOGS] Address field was empty")
            return false
        }
        if (amount == "") {
            showEmptyField("Amount")
            Timber.i("[PADAWANLOGS] Amount field was empty")
            return false
        }

        val addresseesAndAmounts: List<Pair<String, String>> = listOf(Pair(address, amount))
        val feeRate = 1F

        Timber.i("[PADAWANLOGS] Send addresses are: $addresseesAndAmounts")
        return true
    }

    private fun showEmptyField(itemMissing: String) {
        val emptyFieldSnackbar = Snackbar.make(requireView(), "$itemMissing is empty!", Snackbar.LENGTH_LONG)
        emptyFieldSnackbar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.bg0hard))
        emptyFieldSnackbar.view.background = resources.getDrawable(R.drawable.background_toast_warning, null)
        emptyFieldSnackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        emptyFieldSnackbar.show()
    }

    private fun showFeatureInDevelopmentSnackbar() {
        val featureInDevelopmentSnackbar = Snackbar.make(requireView(), "Choosing your fee rate is a feature currently in development!", Snackbar.LENGTH_LONG)
        featureInDevelopmentSnackbar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
        featureInDevelopmentSnackbar.view.background = resources.getDrawable(R.drawable.background_toast, null)
        featureInDevelopmentSnackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        featureInDevelopmentSnackbar.show()
    }
}
