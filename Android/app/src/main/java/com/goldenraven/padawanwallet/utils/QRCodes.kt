/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

private const val TAG = "QrCodes"

class QRCodeAnalyzer(
    private val onQrCodeScanned: (result: String?) -> Unit
) : ImageAnalysis.Analyzer {

    override fun analyze(imageProxy: ImageProxy) {
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        @androidx.camera.core.ExperimentalGetImage
        val mediaImage = imageProxy.image

        if (mediaImage == null) {
            Log.e(TAG, "mediaImage was null")
            return
        }
        try {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val scanner = BarcodeScanning.getClient()

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        val bounds = barcode.boundingBox
                        val corners = barcode.cornerPoints
                        val rawValue = barcode.rawValue
                        val valueType = barcode.valueType
                        Log.i(TAG, "QR code scanned is $rawValue")
                        onQrCodeScanned(rawValue)
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error processing QR code: $it")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing QR code: $e")
        }
    }
}

fun addressToQR(address: String): ImageBitmap? {
    Log.i(TAG, "Generating the QR code for address $address")
    val width = 1000
    val height = 1000
    val hints: Map<EncodeHintType, Any> = mapOf(
        EncodeHintType.CHARACTER_SET to "UTF-8",
        EncodeHintType.QR_VERSION to 7,
        EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H
    )

    try {
        val bitMatrix: BitMatrix = QRCodeWriter().encode(
            address,
            BarcodeFormat.QR_CODE,
            width,
            height,
            hints
        )
        val bitMap = createBitmap(width, height)
        for (x in 0 ..< 1000) {
            for (y in 0 ..< 1000) {
                // uses night1 and md_theme_dark_onPrimary for colors
                bitMap.setPixel(x, y, if (bitMatrix[x, y]) 0xff000000.toInt() else 0x20f3f4ff)
            }
        }
        return bitMap.asImageBitmap()
    } catch (e: Throwable) {
        Log.i(TAG, "Error with QRCode generation, $e")
    }
    return null
}

sealed class QrUiState {
    data object NoQR : QrUiState()
    data object Loading : QrUiState()
    data object QR : QrUiState()
}
