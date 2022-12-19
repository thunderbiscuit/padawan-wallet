package com.goldenraven.padawanwallet.ui.wallet

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.createBitmap
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.PadawanAppBar
import com.goldenraven.padawanwallet.ui.standardBorder
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

private const val TAG = "ReceiveScreen"

@Composable
internal fun ReceiveScreen(
    navController: NavHostController,
    addressViewModel: AddressViewModel = viewModel()
) {
    val address by addressViewModel.address.observeAsState("Generate new address")

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .standardBackground()
    ) {
        val (screenTitle, QRCode, bottomButtons) = createRefs()

        Row(
            Modifier
                .constrainAs(screenTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            PadawanAppBar(
                navController = navController,
                title = "Receive bitcoin",
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .constrainAs(QRCode) {
                    top.linkTo(screenTitle.bottom)
                    bottom.linkTo(bottomButtons.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
        ) {
            val QR: ImageBitmap? = addressToQR(address)
            Log.i(TAG, "New receive address is $address")
            if (address != "No address yet" && QR != null) {
                Image(
                    bitmap = QR,
                    contentDescription = "Bitcoindevkit website QR code",
                    Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                SelectionContainer {
                    Text(
                        text = address,
                        fontFamily = ShareTechMono,
                        fontSize = 12.sp
                    )
                }
            }
        }
        Column(
            Modifier
                .constrainAs(bottomButtons) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 24.dp)
        ) {
            Button(
                onClick = { addressViewModel.updateAddress() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xfff6cf47)),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .standardShadow(20.dp)
            ) {
                Text(
                    text = "Generate a new address",
                )
            }
        }
    }
}

private fun addressToQR(address: String): ImageBitmap? {
    Log.i(TAG, "We are generating the QR code for address $address")
    try {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix: BitMatrix = qrCodeWriter.encode(address, BarcodeFormat.QR_CODE, 1000, 1000)
        val bitMap = createBitmap(1000, 1000)
        for (x in 0 until 1000) {
            for (y in 0 until 1000) {
                // uses night1 and md_theme_dark_onPrimary for colors
                bitMap.setPixel(x, y, if (bitMatrix[x, y]) 0xff000000.toInt() else 0xfff3f4ff.toInt())
                // bitMap.setPixel(x, y, if (bitMatrix[x, y]) 0xFF3c3836.toInt() else 0xFFebdbb2.toInt())
            }
        }
        // Log.i(TAG, "QR is ${bitMap.asImageBitmap()}")
        return bitMap.asImageBitmap()
    } catch (e: Throwable) {
        Log.i(TAG, "Error with QRCode generation, $e")
    }
    return null
}

internal class AddressViewModel : ViewModel() {

    private var _address: MutableLiveData<String> = MutableLiveData("No address yet")
    val address: LiveData<String>
        get() = _address

    //private var _addressIndex: MutableLiveData<UInt> = MutableLiveData(0u)
    //val addressIndex: LiveData<UInt>
    //    get() = _addressIndex

    fun updateAddress() {
        _address.value = Wallet.getLastUnusedAddress().address
        //_addressIndex.value = Wallet.getLastUnusedAddress().index
    }
}