package com.goldenraven.padawanwallet.ui.wallet

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.PadawanAppBar
import com.goldenraven.padawanwallet.ui.standardBorder
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.addressToQR
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "ReceiveScreen"

@Composable
internal fun ReceiveScreen(
    navController: NavHostController,
    viewModel: WalletViewModel
) {
    val address by viewModel.address.observeAsState("Generate new address")
    var QR by remember {
        mutableStateOf<ImageBitmap?>(null)
    }
    val qrUIState: QRUIState = viewModel.QRState.collectAsState(QRUIState.NoQR).value

    LaunchedEffect(address){
        withContext(Dispatchers.IO){
            QR = addressToQR(address)
        }
    }

    val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
        ScreenSizeWidth.Small -> 12.dp
        ScreenSizeWidth.Phone -> 32.dp
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .standardBackground(padding)
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
            PadawanAppBar(navController = navController, title = "Receive bitcoin")
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
            if (qrUIState == QRUIState.Loading) {
                CircularProgressIndicator()
            } else if (qrUIState == QRUIState.QR) {
                QR?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "QR Code",
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
        }
        val bottomPadding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
            ScreenSizeWidth.Small -> 12.dp
            ScreenSizeWidth.Phone -> 24.dp
        }
        Column(
            Modifier
                .constrainAs(bottomButtons) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = bottomPadding)
        ) {
            Button(
                onClick = { viewModel.updateLastUnusedAddress() },
                colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
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

// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewIntroScreen() {
//     PadawanTheme {
//         ReceiveScreen(
//             rememberNavController(),
//             WalletViewModel(),
//         )
//     }
// }
