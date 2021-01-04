/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet.home.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.FragmentWalletHomeBinding
import com.libertysoftware.padawanwallet.home.HomeViewModel
import timber.log.Timber
import java.text.DecimalFormat

class WalletHome : Fragment() {

    private lateinit var binding: FragmentWalletHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletHomeBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel
        val viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        viewModel.balance.observe(viewLifecycleOwner, {
            if (viewModel.satoshiUnit.value == true) {
                val humanReadableBalance = DecimalFormat("###,###,###").format(it).replace(",", "\u2008")
                binding.balance.text = humanReadableBalance
            } else {
                val humanReadableBalance = DecimalFormat("###,##0.00000000").format(it).replace(",", "\u2008")
                binding.balance.text = humanReadableBalance
            }
        })

        binding.syncButton.setOnClickListener {
            viewModel.updateBalance()
            val snackBar = Snackbar.make(view, "Wallet successfully synced", Snackbar.LENGTH_LONG)
            snackBar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
            snackBar.view.background = resources.getDrawable(R.drawable.background_toast, null)
            snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
            snackBar.show()
        }

        binding.unitToggleButton.setOnClickListener {
            Timber.i("[PADAWANLOGS] Toggle unit button was pressed")
            viewModel.changeUnit()
        }

        // navigation
        val navController = Navigation.findNavController(view)

        binding.receiveButton.setOnClickListener {
            navController.navigate(R.id.action_walletHome_to_walletReceive)
        }

        binding.sendButton.setOnClickListener {
            navController.navigate(R.id.action_walletHome_to_walletSend)
        }
    }
}
