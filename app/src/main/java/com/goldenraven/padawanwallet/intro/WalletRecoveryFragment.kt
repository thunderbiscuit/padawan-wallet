/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentRecoverBinding
import com.goldenraven.padawanwallet.home.HomeActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.*

class WalletRecoveryFragment : Fragment() {

    private lateinit var binding: FragmentRecoverBinding
    private lateinit var wordList: List<String>
    private lateinit var mnemonicString: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filename = "bip39-english.txt"
        val inputString: String = requireActivity().applicationContext.assets.open(filename).bufferedReader().use {
            it.readText()
        }

        this.wordList = inputString.split("\n").map { it.trim() }

        view.findViewById<Button>(R.id.buttonRecoverWallet).setOnClickListener {
            if (checkWords()) {
                Wallet.recoverWallet(mnemonicString)

                // launch home activity
                val intent: Intent = Intent(this@WalletRecoveryFragment.context, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Timber.i("[PADAWANLOGS] Mnemonic seed phrase was not valid")
            }
        }
    }

    private fun checkWords(): Boolean {
        val mnemonicWordList: MutableList<String> = mutableListOf()
        val mnemonicWordsTextViews: List<Int> = listOfNotNull<Int>(
                R.id.mnemonicword1, R.id.mnemonicword2, R.id.mnemonicword3, R.id.mnemonicword4, R.id.mnemonicword5, R.id.mnemonicword6,
                R.id.mnemonicword7, R.id.mnemonicword8, R.id.mnemonicword9, R.id.mnemonicword10, R.id.mnemonicword11, R.id.mnemonicword12,
        )

        for (word in 0..11) {
            val mnemonicWord: String = requireView().findViewById<TextView>(mnemonicWordsTextViews[word]).text.toString()
                .trim().toLowerCase(Locale.getDefault())
            Timber.i("[PADAWANLOGS] Word $word: $mnemonicWord")

            if (mnemonicWord.isEmpty()) {
                Timber.i("[PADAWANLOGS] Word #$word is empty!")
                showWordIsEmptySnackbar(word)
                return false
            } else if (mnemonicWord !in this.wordList) {
                Timber.i("[PADAWANLOGS] Word #$word, $mnemonicWord, is not valid!")
                showWordIsInvalidSnackbar(word)
                return false
            } else {
                mnemonicWordList.add(mnemonicWord)
                Timber.i("[PADAWANLOGS] Word #$word, $mnemonicWord is valid")
            }
        }

        Timber.i("[PADAWANLOGS] The final mnemonic is $mnemonicWordList")
        this.mnemonicString = mnemonicWordList.joinToString(" ")
        return true
    }

    private fun showWordIsEmptySnackbar(wordNumber: Int) {
        val wordIsEmptySnackbar = Snackbar.make(requireView(), "Word ${(wordNumber + 1)} is empty!", Snackbar.LENGTH_LONG)
        wordIsEmptySnackbar.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
        wordIsEmptySnackbar.view.background = resources.getDrawable(R.drawable.background_toast_warning, null)
        wordIsEmptySnackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        wordIsEmptySnackbar.show()
    }

    private fun showWordIsInvalidSnackbar(wordNumber: Int) {
        val wordIsInvalid = Snackbar.make(requireView(), "Word ${(wordNumber + 1)} is invalid!", Snackbar.LENGTH_LONG)
        wordIsInvalid.setTextColor(ContextCompat.getColor(requireActivity(), R.color.fg1))
        wordIsInvalid.view.background = resources.getDrawable(R.drawable.background_toast_warning, null)
        wordIsInvalid.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
        wordIsInvalid.show()
    }
}
