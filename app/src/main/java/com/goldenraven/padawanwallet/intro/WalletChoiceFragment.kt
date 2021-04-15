/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentWalletChoiceBinding
import com.goldenraven.padawanwallet.home.HomeActivity

class WalletChoiceFragment : Fragment() {

    private lateinit var binding: FragmentWalletChoiceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWalletChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.createWalletButton.setOnClickListener {
            val intent: Intent = Intent(this@WalletChoiceFragment.context, HomeActivity::class.java)
            Wallet.createWallet()
            startActivity(intent)
        }

        binding.recoverWalletButton.setOnClickListener {
            navController.navigate(R.id.action_walletChoice_to_walletRecoveryFragment)
        }
    }
}
