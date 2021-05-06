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
import com.goldenraven.padawanwallet.databinding.FragmentTutorialsHomeBinding
import com.goldenraven.padawanwallet.home.HomeViewModel
import timber.log.Timber

class TutorialsHome : Fragment() {

    private lateinit var binding: FragmentTutorialsHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTutorialsHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // monitor done flags for tutorials
        val viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        viewModel.retrieveDoneTutorials()

        viewModel.tutorialsDone.observe(viewLifecycleOwner, {
            Timber.i("[PADAWANLOGS] ${viewModel.tutorialsDone.value}")
            if (viewModel.tutorialsDone.value?.get("e1") == true) binding.buttonTutorial1.setBackgroundResource(R.drawable.tutorial_button_done_background)
            if (viewModel.tutorialsDone.value?.get("e2") == true) binding.buttonTutorial2.setBackgroundResource(R.drawable.tutorial_button_done_background)
            if (viewModel.tutorialsDone.value?.get("e3") == true) binding.buttonTutorial3.setBackgroundResource(R.drawable.tutorial_button_done_background)
            if (viewModel.tutorialsDone.value?.get("e4") == true) binding.buttonTutorial4.setBackgroundResource(R.drawable.tutorial_button_done_background)
            if (viewModel.tutorialsDone.value?.get("e5") == true) binding.buttonTutorial5.setBackgroundResource(R.drawable.tutorial_button_done_background)
            if (viewModel.tutorialsDone.value?.get("e6") == true) binding.buttonTutorial6.setBackgroundResource(R.drawable.tutorial_button_done_background)
            if (viewModel.tutorialsDone.value?.get("e7") == true) binding.buttonTutorial7.setBackgroundResource(R.drawable.tutorial_button_done_background)
            if (viewModel.tutorialsDone.value?.get("e8") == true) binding.buttonTutorial8.setBackgroundResource(R.drawable.tutorial_button_done_background)
        })

        // navigation
        val navController = Navigation.findNavController(view)

        binding.buttonTutorial1.setOnClickListener {
            navController.navigate(R.id.action_tutorialsHome_to_tutorialE1)
        }
        binding.buttonTutorial2.setOnClickListener {
            navController.navigate(R.id.action_tutorialsHome_to_tutorialE2)
        }
        binding.buttonTutorial3.setOnClickListener {
            navController.navigate(R.id.action_tutorialsHome_to_tutorialE3)
        }
        binding.buttonTutorial4.setOnClickListener {
            navController.navigate(R.id.action_tutorialsHome_to_tutorialE4)
        }
        binding.buttonTutorial5.setOnClickListener {
            navController.navigate(R.id.action_tutorialsHome_to_tutorialE5)
        }
        binding.buttonTutorial6.setOnClickListener {
            navController.navigate(R.id.action_tutorialsHome_to_tutorialE6)
        }
        binding.buttonTutorial7.setOnClickListener {
            navController.navigate(R.id.action_tutorialsHome_to_tutorialE7)
        }
        binding.buttonTutorial8.setOnClickListener {
            navController.navigate(R.id.action_tutorialsHome_to_tutorialE8)
        }
    }
}
