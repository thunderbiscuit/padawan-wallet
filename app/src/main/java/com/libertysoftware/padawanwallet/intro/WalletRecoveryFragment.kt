/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.libertysoftware.padawanwallet.R
import com.libertysoftware.padawanwallet.databinding.FragmentRecoverBinding
import timber.log.Timber

class WalletRecoveryFragment : Fragment() {

    private lateinit var binding: FragmentRecoverBinding
    private lateinit var wordList: List<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecoverBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filename = "bip39.txt"
        val inputString: String = requireActivity().applicationContext.assets.open(filename).bufferedReader().use {
            it.readText()
        }

        this.wordList = inputString.split("\n").map { it.trim() }
        // Timber.i("[PADAWANLOGS] Word list is $wordList")

        view.findViewById<Button>(R.id.buttonRecoverWallet).setOnClickListener {
            checkWords()
        }
    }

    private fun checkWords(): Boolean {
        val mnemonicWordList: MutableList<String> = mutableListOf()
        val mnemonicWordsTextViews: List<Int> = listOfNotNull<Int>(
                R.id.mnemonicword1, R.id.mnemonicword2, R.id.mnemonicword3, R.id.mnemonicword4, R.id.mnemonicword5, R.id.mnemonicword6,
                R.id.mnemonicword7, R.id.mnemonicword8, R.id.mnemonicword9, R.id.mnemonicword10, R.id.mnemonicword11, R.id.mnemonicword12,
        )

        for (word in 0..11) {
            val mnemonicWord: String = requireView().findViewById<TextView>(mnemonicWordsTextViews[word]).text.toString().trim().toLowerCase()
            Timber.i("[PADAWANLOGS] Word $word: $mnemonicWord")

            if (mnemonicWord.isEmpty()) {
                Timber.i("[PADAWANLOGS] Word #$word is empty!")
                toastWordIsEmpty(word)
                return false
            } else if (mnemonicWord !in this.wordList) {
                Timber.i("[PADAWANLOGS] Word #$word, $mnemonicWord, is not valid!")
                toastWordIsInvalid(word)
                return false
            } else {
                mnemonicWordList.add(mnemonicWord)
                Timber.i("[PADAWANLOGS] Word #$word, $mnemonicWord is valid")
            }
        }

        Timber.i("[PADAWANLOGS] The final mnemonic is $mnemonicWordList")
        return true
    }

    private fun toastWordIsEmpty(wordNumber: Int) {
        val text = "Word ${(wordNumber + 1).toString()} is empty!"
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(this@WalletRecoveryFragment.context, text, duration)
        toast.show()
    }

    private fun toastWordIsInvalid(wordNumber: Int) {
        val text = "Word ${(wordNumber + 1).toString()} is invalid!"
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(this@WalletRecoveryFragment.context, text, duration)
        toast.show()
    }
}