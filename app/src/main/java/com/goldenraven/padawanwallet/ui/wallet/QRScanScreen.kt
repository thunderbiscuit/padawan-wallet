package com.goldenraven.padawanwallet.ui.wallet

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.theme.md_theme_dark_background
import com.goldenraven.padawanwallet.theme.standardShadow
import com.goldenraven.padawanwallet.ui.standardBorder
import com.goldenraven.padawanwallet.utils.QRCodeAnalyzer

@Composable
internal fun QRScanScreen(navController: NavHostController) {

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
        backgroundColor = md_theme_dark_background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
                                            navController.previousBackStackEntry
                                                ?.savedStateHandle
                                                ?.set("BTC_Address", it)
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
                        text = "Cancel",
                        color = Color(0xff000000)
                    )
                }
            }
        }
    }
}