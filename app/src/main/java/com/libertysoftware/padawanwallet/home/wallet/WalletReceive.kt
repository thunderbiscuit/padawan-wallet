/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet.home.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.libertysoftware.padawanwallet.PadawanWalletApplication
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.FragmentWalletReceiveBinding
import timber.log.Timber

class WalletReceive : Fragment() {

    private lateinit var binding: FragmentWalletReceiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletReceiveBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.button4.setOnClickListener {
            navController.navigate(R.id.action_walletReceive_to_walletHome)
        }

        val app = requireActivity().application as PadawanWalletApplication
        val newAddress: String = app.getNewAddress()
        Timber.i("[PADAWANLOGS] New deposit address is $newAddress")
        binding.receiveAddress.text = newAddress

        binding.buttonGenerateNewAddress.setOnClickListener {
            val newGeneratedAddress: String = app.getNewAddress()
            Timber.i("[PADAWANLOGS] New deposit address is $newGeneratedAddress")
            binding.receiveAddress.text = newGeneratedAddress
        }
    }
}
