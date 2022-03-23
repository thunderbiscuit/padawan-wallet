/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet2.wallet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Repository
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentWalletBinding
import com.goldenraven.padawanwallet.utils.SnackbarLevel
import com.goldenraven.padawanwallet.utils.fireSnackbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import android.util.Log

private const val TAG = "WalletFragment"

class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val dialogMessage: String = "We notice it is your first time opening this wallet. Would you like Padawan to send you some testnet coins to get your started?"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstTimePadawanWalletDialog = MaterialAlertDialogBuilder(this@WalletFragment.requireContext(), R.style.MyCustomDialogTheme)
            .setTitle("Hello there!")
            .setMessage(dialogMessage)
            .setPositiveButton("Yes please!") { _, _ ->
                Log.i(TAG,"User would appreciate some testnet coins!")
                val address: String = Wallet.getNewAddress()
                callTatooineFaucet(address)
                Repository.offerFaucetCallDone()
            }
            .setNegativeButton("No thanks") { _, _ ->
                Log.i(TAG,"User doesn't need the coins right now.")
                Repository.offerFaucetCallDone()
            }

        val offerFaucetCallDone =
            context?.getSharedPreferences("current_wallet", Context.MODE_PRIVATE)
                ?.getBoolean("offerFaucetCallDone", false)

        Log.i(TAG,"Value of offerFaucetCallDone is $offerFaucetCallDone")

        if (offerFaucetCallDone == false) {
            firstTimePadawanWalletDialog.show()
        }
    }

    private fun callTatooineFaucet(address: String) {
        val faucetUrl: String = BuildConfig.FAUCET_URL
        val faucetUsername: String = BuildConfig.FAUCET_USERNAME
        val faucetPassword: String = BuildConfig.FAUCET_PASSWORD

        lifecycleScope.launch {
            val ktorClient = HttpClient(CIO) {
                install(Auth) {
                    basic {
                        username = faucetUsername
                        password = faucetPassword
                    }
                }
            }

            Log.i(TAG,"API call to Tatooine will request coins at $address")
            try {
                val response: HttpResponse = ktorClient.post(faucetUrl) {
                    body = TextContent(address, ContentType.Text.Plain)
                }
                Repository.oneTimeFaucetCallDone()
                Log.i(TAG,"API call to Tatooine was performed. Response is ${response.status}, ${response.readText()}")
            } catch (cause: Throwable) {
                Log.i(TAG,"Tatooine call failed: $cause")
                fireSnackbar(
                    requireView(),
                    SnackbarLevel.ERROR,
                    "Error: Faucet Not Available"
                )
            }
            ktorClient.close()
        }
    }
}
