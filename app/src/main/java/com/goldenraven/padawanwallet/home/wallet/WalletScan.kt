/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.home.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.*
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentWalletScanBinding
import timber.log.Timber

// private const val CAMERA_REQUEST_CODE = 101

class WalletScan : Fragment() {
    private lateinit var binding: FragmentWalletScanBinding
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        setupPermissions()
        binding = FragmentWalletScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = Navigation.findNavController(view)

        val scannerView = binding.scannerView
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat, ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.PREVIEW // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                // Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
                Timber.i("[PADAWANLOGS] QR code data is: ${it.text}")
                // navController.navigate(R.id.action_walletScan_to_walletBuild)
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            activity.runOnUiThread {
                Timber.i("[PADAWANLOGS] Camera initialization error: ${it.message}")
            }
        }

        binding.buttonScan.setOnClickListener {
            Timber.i("[PADAWANLOGS] Start scanning from scan button")
            codeScanner.startPreview()
        }
        binding.buttonBackWalletBuild.setOnClickListener {
            navController.navigate(R.id.action_walletScan_to_walletBuild)
        }
        scannerView.setOnClickListener {
            Timber.i("[PADAWANLOGS] Start scanning from scannerView")
            codeScanner.startPreview()
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

//    private fun setupPermissions() {
//        Timber.i("[PADAWANLOGS] Requesting permission step 1")
//        val permission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
//        // val permission = checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
//        Timber.i("[PADAWANLOGS] Requesting permission step 2: $permission")
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            Timber.i("[PADAWANLOGS] Requesting permission step 3, making request")
//            makeRequest()
//        }
//    }
//
//    private fun makeRequest() {
//        Timber.i("[PADAWANLOGS] Requesting permission step 3")
//        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
//        // ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when(requestCode) {
//            CAMERA_REQUEST_CODE -> {
//                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(context, "You need the camera permission to be able to use this feature", Toast.LENGTH_LONG).show()
//                } else {
//                    Timber.i("[PADAWANLOGS] Camera permission granted)
//                }
//            }
//        }
//    }
}