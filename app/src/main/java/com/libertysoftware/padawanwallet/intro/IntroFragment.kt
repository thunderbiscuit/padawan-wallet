package com.libertysoftware.padawanwallet.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.libertysoftware.padawanwallet.R

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    private val dialogMessage: String = "It’s important you know that Padawan is not a bitcoin wallet that can handle normal bitcoins!\n\nThis wallet is built to help you learn how to use bitcoin wallets through a series of tutorials, and it does so using testnet coins, a type of bitcoin that doesn't have any value.\n\nThe wallet can only handle testnet coins, so make sure you don’t send it normal coins!"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        val testnetDialog = this@IntroFragment.context?.let {
            AlertDialog.Builder(it, R.style.MyDialogTheme)
                .setTitle("Be Careful!")
                    .setMessage(dialogMessage)
                    .setPositiveButton("I understand") { _, _ ->
                        navController!!.navigate(R.id.action_introFragment_to_walletChoice)
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        // Toast.makeText(this!!, "Read this page again!", Toast.LENGTH_LONG).show()
                        // log cancel
                    }
        }

        view.findViewById<Button>(R.id.button1).setOnClickListener {
            testnetDialog?.show()
        }
    }
}
