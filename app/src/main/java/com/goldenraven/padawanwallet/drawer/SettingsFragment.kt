/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.goldenraven.padawanwallet.Repository
import com.goldenraven.padawanwallet.databinding.FragmentSettingsBinding
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resetDoneTutorialsButton.setOnClickListener {
            Repository.resetTutorials()
        }

        binding.apiCallButton.setOnClickListener {
            callAPI()
        }
    }

    private fun callAPI() {
        lifecycleScope.launch {
            val ktorClient = HttpClient(CIO)
            val response: HttpResponse = ktorClient.get("http://172.105.14.49:8080")
            Timber.i("[PADAWANLOGS]: API response status is ${response.status}, ${response.readText()}")
            ktorClient.close()
        }
    }
}
