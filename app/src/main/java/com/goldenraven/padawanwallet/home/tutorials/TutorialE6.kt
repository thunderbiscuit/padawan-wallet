/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home.tutorials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentTutorialE6Binding
import com.goldenraven.padawanwallet.home.HomeViewModel

class TutorialE6 : Fragment() {

    private lateinit var binding: FragmentTutorialE6Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTutorialE6Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)
        val viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        binding.buttonMarkDoneE6.setOnClickListener {
            viewModel.markAsDone(tutorialNumber = 6)
            navController.navigate(R.id.action_tutorialE6_to_tutorialsHome)
        }
        binding.buttonBackE6.setOnClickListener {
            navController.navigate(R.id.action_tutorialE6_to_tutorialsHome)
        }
    }
}
