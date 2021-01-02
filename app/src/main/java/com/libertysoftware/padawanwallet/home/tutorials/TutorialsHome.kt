package com.libertysoftware.padawanwallet.home.tutorials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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
    }
}
