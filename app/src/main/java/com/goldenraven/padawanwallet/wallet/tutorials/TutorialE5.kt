/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet.tutorials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentTutorialE5Binding
import com.goldenraven.padawanwallet.wallet.WalletViewModel

class TutorialE5 : Fragment() {

    private lateinit var binding: FragmentTutorialE5Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTutorialE5Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)
        val viewModel = ViewModelProvider(requireActivity()).get(WalletViewModel::class.java)

        binding.buttonMarkDoneE5.setOnClickListener {
            viewModel.markAsDone(tutorialNumber = 5)
            navController.navigate(R.id.action_tutorialE5_to_tutorialsHome)
        }
        binding.buttonBackE5.setOnClickListener {
            navController.navigate(R.id.action_tutorialE5_to_tutorialsHome)
        }
    }
}
