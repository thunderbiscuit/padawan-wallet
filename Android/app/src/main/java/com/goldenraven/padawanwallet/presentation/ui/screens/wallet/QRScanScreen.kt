/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.wallet

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.theme.md_theme_dark_background
import com.goldenraven.padawanwallet.presentation.theme.standardShadow
import com.goldenraven.padawanwallet.presentation.ui.components.standardBorder
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.WalletAction
import com.goldenraven.padawanwallet.utils.QRCodeAnalyzer
import org.bitcointools.bip21.Bip21URI

private const val TAG = "QRScanScreen"

@Composable
internal fun QRScanScreen(
    onAction: (WalletAction) -> Unit,
    navController: NavHostController
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { scaffoldPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            val (camera, cancelButton) = createRefs()

            Box(
                modifier = Modifier
                    .background(md_theme_dark_background)
                    .constrainAs(camera) {
                        top.linkTo(parent.top)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Column {
                    if (hasCameraPermission) {
                        AndroidView(
                            factory = { context ->
                                val previewView = PreviewView(context)
                                val preview = Preview.Builder().build()
                                val selector = CameraSelector.Builder()
                                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                    .build()
                                preview.setSurfaceProvider(previewView.surfaceProvider)
                                val imageAnalysis = ImageAnalysis.Builder()
                                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                    .build()
                                imageAnalysis.setAnalyzer(
                                    ContextCompat.getMainExecutor(context),
                                    QRCodeAnalyzer { result ->
                                        result?.let {
                                            Log.i(TAG, "QR Code detected: $it")

                                            val address = try {
                                                Bip21URI.fromUri(it).address.lowercase()
                                            } catch (e: Exception) {
                                                Log.i(TAG, "BIP21 parsing unsuccessful, $e")
                                                it
                                            }

                                            onAction(WalletAction.QRCodeScanned(address))
                                            // navController.previousBackStackEntry
                                            //     ?.savedStateHandle
                                            //     ?.set("BTC_Address", address)
                                            imageAnalysis.clearAnalyzer()
                                            navController.popBackStack()
                                        }
                                    }
                                )

                                try {
                                    cameraProviderFuture.get().bindToLifecycle(
                                        lifecycleOwner,
                                        selector,
                                        preview,
                                        imageAnalysis
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                return@AndroidView previewView
                            },
                            modifier = Modifier.weight(weight = 1f)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff6cf47)),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .constrainAs(cancelButton) {
                        absoluteRight.linkTo(parent.absoluteRight, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    }
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
                    .standardShadow(20.dp)
                    .height(70.dp)
                    .width(200.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = Color(0xff000000)
                    )
                }
            }
        }
    }
}
