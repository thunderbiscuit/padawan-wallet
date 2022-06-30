package com.goldenraven.padawanwallet.ui.wallet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bitcoindevkit.PartiallySignedBitcoinTransaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SendScreen(navController: NavHostController) {

    val (showDialog, setShowDialog) = rememberSaveable { mutableStateOf(false) }

    val recipientAddress: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val amount: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val feeRate: MutableState<String> = rememberSaveable { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(md_theme_dark_warning),
                    containerColor = md_theme_dark_warning,
                    ) {
                    Text(
                        text = data.visuals.message,
                        style = TextStyle(md_theme_dark_onLightBackground)
                    )
                }
            }
        },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(md_theme_dark_background)
        ) {
            val (screenTitle, transactionInputs, bottomButtons) = createRefs()
            Text(
                text = "Send Bitcoin",
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(screenTitle) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 70.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.constrainAs(transactionInputs) {
                    top.linkTo(screenTitle.bottom)
                    bottom.linkTo(bottomButtons.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
            ) {
                ScanQRCode(navController = navController, recipientAddress = recipientAddress)
                TransactionRecipientInput(recipientAddress)
                TransactionAmountInput(amount)
                TransactionFeeInput(feeRate)
                Dialog(
                    recipientAddress = recipientAddress.value,
                    amount = amount.value,
                    feeRate = feeRate.value,
                    showDialog = showDialog,
                    setShowDialog = setShowDialog,
                    snackbarHostState = snackbarHostState,
                    scope = scope,
                )
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
                    onClick = {
                        val inputVerified = verifyInput(
                            address = recipientAddress.value,
                            amount = amount.value,
                            feeRate = feeRate.value,
                            snackbarHostState = snackbarHostState,
                            scope = scope
                        )
                        if (inputVerified)
                            setShowDialog(true)
                    },
                    colors = ButtonDefaults.buttonColors(md_theme_dark_secondary),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                ) {
                    Text(
                        text = "broadcast transaction",
                        fontFamily = sofiaPro,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun ScanQRCode(navController: NavHostController, recipientAddress: MutableState<String>) {
    val qrCodeScanner = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>("BTC_Address")?.observeAsState()

    qrCodeScanner?.value.let {
        if (it != null)
            recipientAddress.value = it

        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.remove<String>("BTC_Address")
    }

    Row(
        modifier = Modifier.fillMaxWidth(0.8f),
        horizontalArrangement = Arrangement.End,
    ) {
        IconButton(onClick = {
            navController.navigate(Screen.QRScanScreen.route) {
                launchSingleTop = true
            }
        }) {
            Row {
                Icon(
                    contentDescription = null,
                    painter = painterResource(id = R.drawable.ic_camera),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Scan",
                    fontFamily = sofiaPro,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun TransactionRecipientInput(recipientAddress: MutableState<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.9f),
            value = recipientAddress.value,
            onValueChange = { recipientAddress.value = it.trim() },
            label = {
                Text(
                    text = "Recipient address",
                )
            },
            singleLine = true,
            textStyle = TextStyle(fontFamily = shareTechMono, color = md_theme_dark_onBackground),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = md_theme_dark_onBackgroundFaded,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Composable
private fun TransactionAmountInput(amount: MutableState<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.9f),
            value = amount.value,
            onValueChange = { value: String ->
                amount.value = value.filter { it.isDigit() }
            },
            singleLine = true,
            textStyle = TextStyle(fontFamily = shareTechMono, color = md_theme_dark_onBackground),
            label = {
                Text(
                    text = "Amount (satoshis)",
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = md_theme_dark_onBackgroundFaded,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Composable
private fun TransactionFeeInput(feeRate: MutableState<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.9f),
            value = feeRate.value,
            onValueChange = { newValue: String ->
                feeRate.value = newValue.filter { it.isDigit() }
            },
            singleLine = true,
            textStyle = TextStyle(fontFamily = shareTechMono, color = md_theme_dark_onBackground),
            label = {
                Text(
                    text = "Fee rate",
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = md_theme_dark_onBackgroundFaded,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Composable
fun Dialog(
    recipientAddress: String,
    amount: String,
    feeRate: String,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
) {
    if (showDialog) {
        AlertDialog(
            containerColor = md_theme_dark_background2,
            onDismissRequest = {},
            title = {
                Text(
                    text = "Confirm transaction",
                )
            },
            text = {
                Text(
                    text = "Send: $amount satoshis \nto: $recipientAddress\nFee rate: ${feeRate.toFloat()}",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        broadcastTransaction(
                            recipientAddress = recipientAddress,
                            amount = amount.toULong(),
                            feeRate = feeRate.toFloat(),
                            snackbarHostState = snackbarHostState,
                            scope = scope,
                        )
                        setShowDialog(false)
                    },
                ) {
                    Text(
                        text = "Confirm",
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        setShowDialog(false)
                    },
                ) {
                    Text(
                        text = "Cancel",
                    )
                }
            },
        )
    }
}

fun verifyInput(
    address: String,
    amount: String,
    feeRate: String,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
): Boolean {
    if (address.isBlank()) {
        scope.launch {
            snackbarHostState.showSnackbar(message = "Address is missing!", duration = SnackbarDuration.Short)
        }
        return false
    }
    if (amount.isBlank()) {
        scope.launch {
            snackbarHostState.showSnackbar(message = "Amount is missing!", duration = SnackbarDuration.Short)
        }
        return false
    }
    if (feeRate.isBlank()) {
        scope.launch {
            snackbarHostState.showSnackbar(message = "Fee rate is missing!", duration = SnackbarDuration.Short)
        }
        return false
    } else if (feeRate.toInt() < 1 || feeRate.toInt() > 200) {
        scope.launch {
            snackbarHostState.showSnackbar(message = "Please input a fee rate between 1 and 200", duration = SnackbarDuration.Short)
        }
        return false
    }
    return true
}

private fun broadcastTransaction(
    recipientAddress: String,
    amount: ULong,
    feeRate: Float = 1F,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
) {
    Log.i("SendScreen", "Attempting to broadcast transaction with inputs: recipient: $recipientAddress, amount: $amount, fee rate: $feeRate")
    var snackbarMsg = ""
    try {
        // create, sign, and broadcast
        val psbt: PartiallySignedBitcoinTransaction = Wallet.createTransaction(recipientAddress, amount, feeRate)
        Wallet.sign(psbt)
        val txid: String = Wallet.broadcast(psbt)
        snackbarMsg = "Transaction was broadcast successfully"
        Log.i("SendScreen", "Transaction was broadcast! txid: $txid")
    } catch (e: Throwable) {
        Log.i("SendScreen", "Broadcast error: ${e.message}")
        snackbarMsg = "Error : ${e.message}"
    }
    scope.launch {
        snackbarHostState.showSnackbar(message = snackbarMsg, duration = SnackbarDuration.Short)
    }
}
