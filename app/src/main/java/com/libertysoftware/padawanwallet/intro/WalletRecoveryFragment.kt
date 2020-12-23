/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.libertysoftware.padawanwallet.databinding.FragmentRecoverBinding

class WalletRecoveryFragment : Fragment() {

    private lateinit var binding: FragmentRecoverBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecoverBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }
}