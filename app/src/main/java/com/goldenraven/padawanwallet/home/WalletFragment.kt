/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.Repository
import com.goldenraven.padawanwallet.databinding.FragmentWalletBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    private val dialogMessage: String = "We notice it is your first time opening this wallet. Would you like Padawan to send you some testnet coins to get your started?"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstTimePadawanWalletDialog = MaterialAlertDialogBuilder(this@WalletFragment.requireContext(), R.style.MyCustomDialogTheme)
            .setTitle("Hello there!")
            .setMessage(dialogMessage)
            .setPositiveButton("Yes please!") { _, _ ->
                Timber.i("[PADAWANLOGS] User would appreciate some testnet coins!")
                // requestCoinsTatooine()
                Repository.oneTimeFaucetCallDone()
            }
            .setNegativeButton("No thanks") { _, _ ->
                Timber.i("[PADAWANLOGS] User doesn't need the coins right now.")
                Repository.oneTimeFaucetCallDone()
            }

        val oneTimeCallTatooine =
            context?.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
                ?.getBoolean("oneTimeFaucetCallDone", false)

        Timber.i("[PADAWANLOGS] Value of oneTimeFaucetCallDone is $oneTimeCallTatooine")

        if (oneTimeCallTatooine == false) {
            firstTimePadawanWalletDialog.show()
        }
    }
}
