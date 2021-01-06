/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.goldenraven.padawanwallet.databinding.FragmentWalletBinding

class WalletFragment : Fragment() {

    private lateinit var binding: FragmentWalletBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // return inflater.inflate(R.layout.fragment_wallet, container, false)
        binding = FragmentWalletBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.button4.setOnClickListener {
//            showNewAddressToast()
//        }
//    }

//    private fun showNewAddressToast(): Unit {
//
//        val app = requireActivity().application as PadawanWalletApplication
//        val newAddress: String = app.getNewAddress()
//        Timber.i("[PADAWANLOGS] New deposit address is $newAddress")
//
//        val text = "New deposit address is $newAddress"
//        val duration = Toast.LENGTH_SHORT
//        val toast = Toast.makeText(this@WalletFragment.context, text, duration)
//        toast.show()
//    }
}
