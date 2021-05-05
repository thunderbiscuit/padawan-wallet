/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home.wallet

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.*
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentWalletScanBinding
import timber.log.Timber

private const val CAMERA_REQUEST_CODE = 101

class WalletScan : Fragment() {
    private lateinit var binding: FragmentWalletScanBinding
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupPermissions()
        codeScanner()

        val navController = Navigation.findNavController(view)

        binding.buttonScan.setOnClickListener {
            codeScanner.startPreview()
        }
        binding.buttonBackWalletBuild.setOnClickListener {
            navController.navigate(R.id.action_walletScan_to_walletBuild)
        }
    }

    private fun codeScanner() {
        val scannerView = binding.scannerView
        codeScanner = CodeScanner(requireActivity(), scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    Log.i("Main", "[PADAWANLOGS] Code scanner scanned ${it.text}")
                    Toast.makeText(
                        requireContext(),
                        "Address is ${it.text}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            errorCallback = ErrorCallback {
                requireActivity().runOnUiThread {
                    Log.i("Main", "[PADAWANLOGS] Camera initialization error: ${it.message}")
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions() {
        Timber.i("[PADAWANLOGS] Requesting permission step 1")
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        Log.i("Main", "[PADAWANLOGS] Requesting permission step 2: $permission")

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("Main", "[PADAWANLOGS] Requesting permission step 3, making request")
            makeRequest()
        }
    }

    private fun makeRequest() {
        Log.i("Main", "[PADAWANLOGS] Requesting permission step 3")
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        requireContext(),
                        "You need the camera permission to be able to use this feature",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.i("Main", "[PADAWANLOGS] Camera permission granted")
                }
            }
        }
    }
}
