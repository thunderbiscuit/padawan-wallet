/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet.wallet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentWalletReceiveBinding
import timber.log.Timber

class WalletReceive : Fragment() {

    private lateinit var binding: FragmentWalletReceiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletReceiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(view)

        binding.button4.setOnClickListener {
            navController.navigate(R.id.action_walletReceive_to_walletHome)
        }

        binding.buttonGenerateNewAddress.setOnClickListener {
            val newGeneratedAddress: String = Wallet.getNewAddress()
            Timber.i("[PADAWANLOGS] New deposit address is $newGeneratedAddress")

            val qrgEncoder: QRGEncoder = QRGEncoder(newGeneratedAddress, null, QRGContents.Type.TEXT, 1000)
            qrgEncoder.colorBlack = resources.getColor(R.color.fg4)
            qrgEncoder.colorWhite = resources.getColor(R.color.bg0soft)
            try {
                val bitmap = qrgEncoder.bitmap
                binding.qrCode.setImageBitmap(bitmap)
            } catch (e: Throwable) {
                Timber.i("[PADAWANLOGS] Error with QRCode generator, ${e.toString()}")
            }
            binding.receiveAddress.text = newGeneratedAddress
        }

        binding.copyAddressButton.setOnClickListener {
            val clipboard: ClipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("Copied address", binding.receiveAddress.text)
            clipboard.setPrimaryClip(clip)
        }
    }
}
