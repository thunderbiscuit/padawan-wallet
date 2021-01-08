/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentIntroBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    private val dialogMessage: String = "It’s important you know that Padawan is not a bitcoin wallet that can handle normal bitcoins.\n\nThis wallet is built to help you learn and experiment with bitcoin wallets through a series of tutorials, and it uses testnet coins, a type of bitcoin that doesn't have any value.\n\nThe wallet can only handle testnet coins, so make sure you do not send it normal bitcoins!"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        val testnetDialog = MaterialAlertDialogBuilder(this@IntroFragment.requireContext(), R.style.MyCustomDialogTheme)
                                .setTitle("Be careful!")
                                .setMessage(dialogMessage)
                                .setPositiveButton("I understand") { _, _ ->
                                    Timber.i("[PADAWANLOGS] User confirms they understands it's a testnet only wallet")
                                    navController.navigate(R.id.action_introFragment_to_walletChoice)
                                }
                                .setNegativeButton("Cancel") { _, _ ->
                                    // Toast.makeText(this!!, "Read this page again!", Toast.LENGTH_LONG).show()
                                }

//        val testnetDialog = this@IntroFragment.context?.let {
//            AlertDialog.Builder(it, R.style.MyDialogTheme)
//                .setTitle("Be Careful!")
//                    .setMessage(dialogMessage)
//                    .setPositiveButton("I understand") { _, _ ->
//                        Timber.i("[PADAWANLOGS] User confirms they understands it's a testnet only wallet")
//                        navController.navigate(R.id.action_introFragment_to_walletChoice)
//                    }
//                    .setNegativeButton("Cancel") { _, _ ->
//                        // Toast.makeText(this!!, "Read this page again!", Toast.LENGTH_LONG).show()
//                    }
//        }

        view.findViewById<Button>(R.id.button1).setOnClickListener {
            Timber.i("[PADAWANLOGS] User is ready to go!")
            // testnetDialog?.show()
            testnetDialog.show()
        }
    }
}
