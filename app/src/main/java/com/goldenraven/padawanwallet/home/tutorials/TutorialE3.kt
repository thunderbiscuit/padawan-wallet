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
import com.goldenraven.padawanwallet.databinding.FragmentTutorialE3Binding
import com.goldenraven.padawanwallet.home.HomeViewModel

class TutorialE3 : Fragment() {

    private lateinit var binding: FragmentTutorialE3Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTutorialE3Binding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)
        val viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        binding.buttonMarkDoneE3.setOnClickListener {
            viewModel.markAsDone(tutorialNumber = 3)
            navController.navigate(R.id.action_tutorialE3_to_tutorialsHome)
        }
        binding.buttonBackE3.setOnClickListener {
            navController.navigate(R.id.action_tutorialE3_to_tutorialsHome)
        }
    }
}
