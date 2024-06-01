/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import com.google.zxing.*
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.nio.ByteBuffer

private const val TAG = "QrCodes"

class QRCodeAnalyzer(
    private val onQrCodeScanned: (result: String?) -> Unit
) : ImageAnalysis.Analyzer {

    companion object {
        private val SUPPORTED_IMAGE_FORMATS = listOf(ImageFormat.YUV_420_888, ImageFormat.YUV_422_888, ImageFormat.YUV_444_888)
    }

    override fun analyze(image: ImageProxy) {
        if (image.format in SUPPORTED_IMAGE_FORMATS) {
            val bytes = image.planes.first().buffer.toByteArray()
            val source = PlanarYUVLuminanceSource(
                bytes,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = MultiFormatReader().apply {
                    setHints(
                        mapOf(
                            DecodeHintType.POSSIBLE_FORMATS to listOf(BarcodeFormat.QR_CODE)
                        )
                    )
                }.decode(binaryBitmap)
                Log.i(TAG, "QR code scanned is ${result.text}")
                onQrCodeScanned(result.text)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                image.close()
            }
        }
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        return ByteArray(remaining()).also { get(it) }
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
