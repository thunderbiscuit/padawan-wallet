/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.drawer

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Repository
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentSettingsBinding
import com.goldenraven.padawanwallet.utils.SnackbarLevel
import com.goldenraven.padawanwallet.utils.fireSnackbar
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.launch

private const val TAG = "SettingsFragment"

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val toolBarTitle = requireActivity().findViewById<TextView>(R.id.toolbarTitleDrawer)
        toolBarTitle.text = getString(R.string.settings_title)

        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resetDoneTutorialsButton.setOnClickListener {
            Repository.resetTutorials()
        }

        when (Repository.wasOneTimeFaucetCallDone()) {
            true -> {
                // binding.apiCallButton.setBackgroundColor(Color.argb(50, 250, 189, 47))
                binding.apiCallButton.isEnabled = false
            }
            false -> {
                binding.apiCallButton.setBackgroundColor(Color.argb(255, 250, 189, 47))
                binding.apiCallButton.setOnClickListener {
                    val address: String = Wallet.getNewAddress()
                    callTatooineFaucet(address)
                }
            }
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

            Log.i(TAG, "API call to Tatooine will request coins at $address")
            try {
                val response: HttpResponse = ktorClient.post(faucetUrl) {
                    body = TextContent(address, ContentType.Text.Plain)
                }
                Repository.oneTimeFaucetCallDone()
                Log.i(TAG, "API call to Tatooine was performed. Response is ${response.status}, ${response.readText()}")
            } catch (cause: Throwable) {
                Log.i(TAG, "Tatooine call failed: $cause")
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
