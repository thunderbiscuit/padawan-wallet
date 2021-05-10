/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.SnackbarLevel
import com.goldenraven.padawanwallet.databinding.FragmentWalletHomeBinding
import com.goldenraven.padawanwallet.fireSnackbar
import com.goldenraven.padawanwallet.home.HomeViewModel
import com.goldenraven.padawanwallet.home.TxHistoryAdapter
import timber.log.Timber
import java.text.DecimalFormat

class WalletHome : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentWalletHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletHomeBinding.inflate(inflater, container, false)

        // RecyclerView
        val adapter = TxHistoryAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ViewModel
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        viewModel.readAllData.observe(viewLifecycleOwner, Observer { tx ->
            adapter.setData(tx)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel
        // viewModel moved up in onCreateView
        // val viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        viewModel.balance.observe(viewLifecycleOwner, {
            if (viewModel.satoshiUnit.value == true) {
                val balanceInSatoshis = it
                val humanReadableBalance = DecimalFormat("###,###,###").format(balanceInSatoshis).replace(",", "\u2008")
                binding.balance.text = humanReadableBalance
                // binding.unitToggleButton.text = "sat"
                // binding.unitToggleButton.textSize = 28F
                // binding.unitToggleButton.setTypeface()
                binding.unitToggleButton.text = ""
                binding.unitToggleButton.background = resources.getDrawable(R.drawable.background_unit_satoshi, null)
            } else {
                var balanceInBitcoin: Float
                if (it == 0L) {
                    balanceInBitcoin = 0F
                } else {
                    balanceInBitcoin = it.toFloat().div(100_000_000)
                }
                val humanReadableBalance = DecimalFormat("0.00000000").format(balanceInBitcoin)
                binding.balance.text = humanReadableBalance
                binding.unitToggleButton.background = resources.getDrawable(R.drawable.background_unit_bitcoin, null)
                binding.unitToggleButton.textSize = 42F
                binding.unitToggleButton.text = "Ƀ"
            }
        })

        binding.syncButton.setOnClickListener {
            viewModel.updateBalance()
            fireSnackbar(
                requireView(),
                SnackbarLevel.INFO,
                "Wallet successfully synced!"
            )
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
