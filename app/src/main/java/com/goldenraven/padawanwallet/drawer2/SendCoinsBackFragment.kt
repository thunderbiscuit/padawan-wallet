/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.drawer2

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentSendCoinsBackBinding
import com.goldenraven.padawanwallet.utils.SnackbarLevel
import com.goldenraven.padawanwallet.utils.fireSnackbar

class SendCoinsBackFragment : Fragment() {

    private lateinit var binding: FragmentSendCoinsBackBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val toolBarTitle = requireActivity().findViewById<TextView>(R.id.toolbarTitleDrawer)
        toolBarTitle.text = getString(R.string.send_coins_back_title)
        binding = FragmentSendCoinsBackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.copyAddressFaucetButton.setOnClickListener {
            val clipboard: ClipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copied address", binding.returnFaucetAddress.text)
            clipboard.setPrimaryClip(clip)
            fireSnackbar(
                requireView(),
                SnackbarLevel.INFO,
                "Copied address to clipboard!"
            )
        }
    }
}