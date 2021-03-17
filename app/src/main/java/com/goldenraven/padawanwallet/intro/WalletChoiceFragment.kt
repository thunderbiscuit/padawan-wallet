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
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentWalletChoiceBinding
import com.goldenraven.padawanwallet.home.HomeActivity

class WalletChoiceFragment : Fragment() {

    private lateinit var binding: FragmentWalletChoiceBinding
    // private lateinit var keys: ExtendedKeys

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWalletChoiceBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        view.findViewById<Button>(R.id.create_wallet_button).setOnClickListener {
            val intent: Intent = Intent(this@WalletChoiceFragment.context, HomeActivity::class.java)
            // generateWallet()
            // val editor: SharedPreferences.Editor = this.requireActivity().getSharedPreferences("current_wallet", Context.MODE_PRIVATE)!!.edit()
            Wallet.createWallet()
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.recover_wallet_button).setOnClickListener {
            navController.navigate(R.id.action_walletChoice_to_walletRecoveryFragment)
        }
    }
}
