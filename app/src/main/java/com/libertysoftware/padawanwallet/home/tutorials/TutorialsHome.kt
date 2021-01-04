/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet.home.tutorials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.FragmentTutorialsHomeBinding

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
            val snackBar = Snackbar.make(view, "In development", Snackbar.LENGTH_LONG)
            snackBar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.bright_yellow))
            snackBar.show()
        }

        binding.buttonTutorial4.setOnClickListener {
            val snackBar = Snackbar.make(view, "In development", Snackbar.LENGTH_LONG)
            // snackBar.view.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.bright_yellow))
            snackBar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
            snackBar.view.background = resources.getDrawable(R.drawable.background_toast, null)
            snackBar.show()
        }
        binding.buttonTutorial5.setOnClickListener {
            val button5Snackbar = Snackbar.make(view, "New default snackbar", Snackbar.LENGTH_LONG)
            button5Snackbar.setBackgroundTint(ContextCompat.getColor(requireActivity(), R.color.aqua))
            button5Snackbar.show()
        }
    }
}
