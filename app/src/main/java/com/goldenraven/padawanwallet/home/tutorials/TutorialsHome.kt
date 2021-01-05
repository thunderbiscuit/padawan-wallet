/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home.tutorials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentTutorialsHomeBinding

class TutorialsHome : Fragment() {

    private lateinit var binding: FragmentTutorialsHomeBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTutorialsHomeBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.buttonTutorial1.setOnClickListener {
            navController.navigate(R.id.action_tutorialsHome_to_tutorialE1)
        }
        binding.buttonTutorial2.setOnClickListener {
            navController.navigate(R.id.action_tutorialsHome_to_tutorialE2)
        }
        binding.buttonTutorial3.setOnClickListener {
            showTutorialInDevelopmentSnackbar()
        }
        binding.buttonTutorial4.setOnClickListener {
            showTutorialInDevelopmentSnackbar()
        }
        binding.buttonTutorial5.setOnClickListener {
            showTutorialInDevelopmentSnackbar()
        }
        binding.buttonTutorial6.setOnClickListener {
            showTutorialInDevelopmentSnackbar()
        }
        binding.buttonTutorial7.setOnClickListener {
            showTutorialInDevelopmentSnackbar()
        }
        binding.buttonTutorial8.setOnClickListener {
            showTutorialInDevelopmentSnackbar()
        }
    }

    private fun showTutorialInDevelopmentSnackbar() {
        val tutorialInDevelopmentSnackbar = Snackbar.make(requireView(), "This tutorial is in development!", Snackbar.LENGTH_LONG)
        tutorialInDevelopmentSnackbar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
        tutorialInDevelopmentSnackbar.view.background = resources.getDrawable(R.drawable.background_toast, null)
        tutorialInDevelopmentSnackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        tutorialInDevelopmentSnackbar.show()
    }

}
