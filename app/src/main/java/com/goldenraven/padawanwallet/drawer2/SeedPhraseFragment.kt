/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.drawer2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Repository
import com.goldenraven.padawanwallet.databinding.FragmentSeedphraseBinding

class SeedPhraseFragment : Fragment() {

    private lateinit var binding: FragmentSeedphraseBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val toolBarTitle = requireActivity().findViewById<TextView>(R.id.toolbarTitleDrawer)
        toolBarTitle.text = getString(R.string.recovery_phrase_title)

        binding = FragmentSeedphraseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        populateMnemonic()
    }

    private fun populateMnemonic() {
        val seedPhrase: String = Repository.getMnemonic()
        val wordList: List<String> = seedPhrase.split(" ")
        val viewList: List<TextView> = listOfNotNull<TextView>(
            binding.displayMnemonicWord1, binding.displayMnemonicWord2, binding.displayMnemonicWord3, binding.displayMnemonicWord4,
            binding.displayMnemonicWord5, binding.displayMnemonicWord6, binding.displayMnemonicWord7, binding.displayMnemonicWord8,
            binding.displayMnemonicWord9, binding.displayMnemonicWord10, binding.displayMnemonicWord11, binding.displayMnemonicWord12,
        )

        for (word in 0..11) {
            val mnemonicWord: String = wordList[word]
            viewList[word].text = ("${word + 1}. $mnemonicWord")
        }
    }
}
