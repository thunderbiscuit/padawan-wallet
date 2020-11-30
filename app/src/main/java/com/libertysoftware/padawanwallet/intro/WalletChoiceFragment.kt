package com.libertysoftware.padawanwallet.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.libertysoftware.padawanwallet.PadawanWalletApplication
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.main.MainActivity
import timber.log.Timber

class WalletChoiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet_choice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button2).setOnClickListener {
            val intent: Intent = Intent(this@WalletChoiceFragment.context, MainActivity::class.java)
            showSeedToast()
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.button3).setOnClickListener {
            showDevelopmentToast()
        }
    }

    fun showSeedToast() {
        val seedWords: String = generateSeedWords().toString()
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(this@WalletChoiceFragment.context, seedWords, duration)
        toast.show()
    }

    fun showDevelopmentToast() {
        val text = "Currently in development..."
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(this@WalletChoiceFragment.context, text, duration)
        toast.show()
    }

    fun generateSeedWords(): List<String> {
        val app = requireActivity().application as PadawanWalletApplication
        val keys = app.generateExtendedKey(12)
        val seedWords: List<String> = keys.mnemonic.split(' ')
        Timber.i(seedWords.toString())
        return seedWords
    }
}
