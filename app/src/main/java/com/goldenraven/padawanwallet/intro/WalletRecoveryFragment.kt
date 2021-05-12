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
import androidx.fragment.app.Fragment
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.utils.*
import com.goldenraven.padawanwallet.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentRecoverBinding
import com.goldenraven.padawanwallet.wallet.WalletActivity
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
                val intent: Intent = Intent(this@WalletRecoveryFragment.context, WalletActivity::class.java)
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
                // val message: String = "Word #${word + 1} is empty!"
                fireSnackbar(
                    requireView(),
                    SnackbarLevel.WARNING,
                    "Word #${word + 1} is empty!"
                )
                return false
            } else if (mnemonicWord !in this.wordList) {
                Timber.i("[PADAWANLOGS] Word #$word, $mnemonicWord, is not valid!")
                fireSnackbar(
                    requireView(),
                    SnackbarLevel.WARNING,
                    "Word #${word + 1} is invalid!"
                )
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
}
