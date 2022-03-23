/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.wallet2.wallet.wallet

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.budiyev.android.codescanner.*
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.databinding.FragmentWalletScanBinding
import android.util.Log

private const val CAMERA_REQUEST_CODE = 101
private const val TAG = "WalletScan"

class WalletScan : Fragment() {
    private lateinit var binding: FragmentWalletScanBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var navController: NavController

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

        navController = Navigation.findNavController(view)

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
                    Log.i(TAG,"Code scanner scanned ${it.text}")
                    val bundle = bundleOf("addressFromScanner" to it.text)
                    navController.navigate(R.id.action_walletScan_to_walletBuild, bundle)
                }
            }

            errorCallback = ErrorCallback {
                requireActivity().runOnUiThread {
                    Log.i(TAG,"Camera initialization error: ${it.message}")
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
        Log.i(TAG,"Requesting permission step 1")
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        Log.i(TAG,"Requesting permission step 2: $permission")

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG,"Requesting permission step 3, making request")
            makeRequest()
        }
    }

    private fun makeRequest() {
        Log.i(TAG,"Requesting permission step 3")
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
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
                    Log.i(TAG,"Camera permission granted")
                }
            }
        }
    }
}
