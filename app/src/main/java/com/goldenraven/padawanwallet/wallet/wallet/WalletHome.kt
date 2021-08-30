/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet.wallet

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentWalletHomeBinding
import com.goldenraven.padawanwallet.utils.SnackbarLevel
import com.goldenraven.padawanwallet.utils.fireSnackbar
import com.goldenraven.padawanwallet.utils.isNetworkAvailable
import com.goldenraven.padawanwallet.wallet.TxHistoryAdapter
import com.goldenraven.padawanwallet.wallet.WalletViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tourguide.tourguide.Overlay
import tourguide.tourguide.ToolTip
import tourguide.tourguide.TourGuide
import android.widget.Button
import android.widget.TextView
import com.goldenraven.padawanwallet.data.Repository
=======
>>>>>>> 507d6c573617951002fe0a7db9ad537da1255e70
import java.text.DecimalFormat
class WalletHome : Fragment() {

    private lateinit var viewModel: WalletViewModel
    private lateinit var tour : TourGuide
    private lateinit var tourG : TourGuide
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
        viewModel = ViewModelProvider(requireActivity()).get(WalletViewModel::class.java)
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

        val offerFaucetCallDone =
            context?.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
                ?.getBoolean("offerFaucetCallDone", false)

        //TourGuide beginning
        if (offerFaucetCallDone == false)
            createTour("Sync Balance", "Click on sync button to view current balance and transaction history", binding.syncButton)

        binding.syncButton.setOnClickListener {
            when (isNetworkAvailable(requireContext())) {
                true -> {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.updateBalance()
                        viewModel.syncTransactionHistory()
                        withContext(Dispatchers.Main) {
                            fireSnackbar(requireView(), SnackbarLevel.INFO, "Wallet successfully synced!")
                        }
                    }
                }
                false -> fireSnackbar(requireView(), SnackbarLevel.WARNING, "Network connection currently not available")
            }
            if (offerFaucetCallDone == false) {
                tour.cleanUp()
                createTourText("Change Unit", "You can always change Santoshi unit to bitcoin and vice versa", binding.unitToggleButton)
            }
        }

        binding.unitToggleButton.setOnClickListener {
            Log.i("Padalogs","Toggle unit button was pressed")
            viewModel.changeUnit()
            if (offerFaucetCallDone == false) {
                tourG.cleanUp()
                createTour("Send Coins", "Click on send button to give testnet coins to your family and friends or you can also give the testnet coins back to us(checkout the menu to find our address)", binding.sendButton)
            }
        }
        // navigation
        val navController = Navigation.findNavController(view)

        binding.receiveButton.setOnClickListener {
            navController.navigate(R.id.action_walletHome_to_walletReceive)
        }

        binding.sendButton.setOnClickListener {
            if (offerFaucetCallDone == false) {
                tour.cleanUp()
                Repository.homeTutorialDone()
            }
            navController.navigate(R.id.action_walletHome_to_walletSend)
        }
        val homeTourDone =
                context?.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
                        ?.getBoolean("homeTourDone", false)
            if (offerFaucetCallDone == false)
                tour.cleanUp()
            navController.navigate(R.id.action_walletHome_to_walletSend)
        }

    }

    private fun createTour(heading : String, message : String, button : Button) {
        tour = TourGuide.init(activity)
            .setToolTip(ToolTip()
                .setTitle(heading)
                .setDescription(message)
                .setBackgroundColor(Color.parseColor("#7c6f64"))
                .setTextColor(Color.parseColor("#d5c4a1"))
                )
            .setOverlay(Overlay())
            .playOn(button)
    }
    private fun createTourText(heading : String, message : String, button : TextView) {
        tourG = TourGuide.init(activity)
            .setToolTip(ToolTip()
                .setTitle(heading)
                .setDescription(message)
                .setBackgroundColor(Color.parseColor("#7c6f64"))
                .setTextColor(Color.parseColor("#d5c4a1"))
            )
            .setOverlay(Overlay())
            .playOn(button)
    }
}
