/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
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
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.databinding.FragmentWalletReceiveBinding
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.goldenraven.padawanwallet.utils.SnackbarLevel
import com.goldenraven.padawanwallet.utils.fireSnackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

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
            Log.i("Padalogs","New deposit address is $newGeneratedAddress")

            try {
                val qrCodeWriter: QRCodeWriter = QRCodeWriter()
                val bitMatrix: BitMatrix = qrCodeWriter.encode(newGeneratedAddress, BarcodeFormat.QR_CODE, 1000, 1000)
                val bitMap = createBitmap(1000, 1000)
                for (x in 0 until 1000) {
                    for (y in 0 until 1000) {
                        bitMap.setPixel(x, y, if (bitMatrix[x, y]) ContextCompat.getColor(requireContext(), R.color.fg4) else ContextCompat.getColor(requireContext(), R.color.bg0soft))
                    }
                }
                binding.qrCode.setImageBitmap(bitMap)
            } catch (e: Throwable) {
                Log.i("Padalogs", "Error with QRCode generation, $e")
            }
            binding.receiveAddress.text = newGeneratedAddress
        }

        binding.copyAddressButton.setOnClickListener {
            if (binding.receiveAddress.text.toString() != "") {
                val clipboard: ClipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("Copied address", binding.receiveAddress.text)
                clipboard.setPrimaryClip(clip)
                fireSnackbar(
                    requireView(),
                    SnackbarLevel.INFO,
                    "Copied address to clipboard!"
                )
            } else {
                fireSnackbar(
                    requireView(),
                    SnackbarLevel.INFO,
                    "Generate new address first!"
                )
            }
        }
    }
}
