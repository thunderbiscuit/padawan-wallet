/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bitcoindevkit.PartiallySignedBitcoinTransaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SendScreen(navController: NavHostController, walletViewModel: WalletViewModel) {
    val (showDialog, setShowDialog) = rememberSaveable { mutableStateOf(false) }

    val balance by walletViewModel.balance.observeAsState()
    val recipientAddress: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val amount: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val feeRate: MutableState<String> = rememberSaveable { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val transactionOptions = remember {
        listOf(
            TransactionType.DEFAULT,
            TransactionType.SEND_ALL,
        )
    }
    val transactionOption: MutableState<TransactionType> = rememberSaveable { mutableStateOf(transactionOptions[0]) }
    val showMenu: MutableState<Boolean> = remember { mutableStateOf(false) }

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
        Column(modifier = Modifier.standardBackground()) {
            Row(modifier = Modifier.padding(top = 120.dp, bottom = 8.dp)) {
                Text(
                    text = "Amount",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .weight(weight = 0.5f)
                        .align(Alignment.Bottom)
                )
                Text(
                    text = "Balance: ${balance.toString()} sats",
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(weight = 0.5f)
                        .align(Alignment.Bottom)
                )
            }
            TextField(
                modifier = Modifier.wideTextField().height(IntrinsicSize.Min),
                shape = RoundedCornerShape(percent = 20),
                value = if (transactionOption.value == TransactionType.DEFAULT) amount.value else "${Wallet.getBalance()} (Before Fees)",
                onValueChange = { value -> amount.value = value.filter { it.isDigit() } },
                singleLine = true,
                placeholder = { Text(text = "Enter Amount") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = padawan_theme_background_secondary,
                    cursorColor = padawan_theme_onPrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                enabled = (
                    when (transactionOption.value) {
                        TransactionType.SEND_ALL -> false
                        else -> true
                    }
                ),
                trailingIcon = {
                    Row {
                        VerticalDivider()
                        Icon(
                            painter = painterResource(id = R.drawable.ic_send),
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                }
            )
//            val (screenTitle, transactionInputs, bottomButtons, dropDownMenu) = createRefs()
//            Text(
//                text = "Send Bitcoin",
//                fontSize = 28.sp,
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .constrainAs(screenTitle) {
//                        top.linkTo(parent.top)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//                    .padding(top = 70.dp)
//            )
//
//
//            Column(
//                modifier = Modifier
//                    .constrainAs(dropDownMenu) {
//                        start.linkTo(screenTitle.end)
//                    }
//                    .padding(top = 67.dp),
//            ) {
//                IconButton(onClick = { showMenu.value = !showMenu.value }) {
//                    Icon(
//                        imageVector = Icons.Default.Settings,
//                        contentDescription = "More transaction options",
//                    )
//                }
//                DropdownMenu(
//                    expanded = showMenu.value,
//                    onDismissRequest = { showMenu.value = false }
//                ) {
//                    DropdownMenuItem(
//                        onClick = {
//                            transactionOption.value = TransactionType.DEFAULT
//                            showMenu.value = false
//                        },
//                        text = {
//                            if (transactionOption.value == TransactionType.DEFAULT) {
//                                Text("Default ✓")
//                            } else {
//                                Text(text = "Default")
//                            }
//                        }
//                    )
//                    DropdownMenuItem(
//                        onClick = {
//                            transactionOption.value = TransactionType.SEND_ALL
//                            showMenu.value = false
//                        },
//                        text = {
//                            if (transactionOption.value == TransactionType.SEND_ALL) {
//                                Text(text = "Send All ✓")
//                            } else {
//                                Text(text = "Send All")
//                            }
//                        }
//                    )
//                }
//            }
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier.constrainAs(transactionInputs) {
//                    top.linkTo(screenTitle.bottom)
//                    bottom.linkTo(bottomButtons.top)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    height = Dimension.fillToConstraints
//                }
//            ) {
//                ScanQRCode(navController = navController, recipientAddress = recipientAddress)
//                TransactionRecipientInput(recipientAddress = recipientAddress)
//                TransactionAmountInput(amount = amount, transactionOption = transactionOption.value)
//                TransactionFeeInput(feeRate = feeRate)
//                Dialog(
//                    recipientAddress = recipientAddress.value,
//                    amount = amount.value,
//                    feeRate = feeRate.value,
//                    transactionOption = transactionOption.value,
//                    showDialog = showDialog,
//                    setShowDialog = setShowDialog,
//                    snackbarHostState = snackbarHostState,
//                    scope = scope,
//                )
//            }
//
//            Column(
//                Modifier
//                    .constrainAs(bottomButtons) {
//                        bottom.linkTo(parent.bottom)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//                    .padding(bottom = 24.dp)
//            ) {
//                Button(
//                    onClick = {
//                        val inputVerified = verifyInput(
//                            recipientAddress = recipientAddress.value,
//                            amount = amount.value,
//                            feeRate = feeRate.value,
//                            transactionOption = transactionOption.value,
//                            snackbarHostState = snackbarHostState,
//                            scope = scope
//                        )
//                        if (inputVerified)
//                            setShowDialog(true)
//                    },
//                    colors = ButtonDefaults.buttonColors(md_theme_dark_secondary),
//                    shape = RoundedCornerShape(16.dp),
//                    modifier = Modifier
//                        .height(80.dp)
//                        .fillMaxWidth(0.9f)
//                        .padding(vertical = 8.dp, horizontal = 8.dp)
//                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
//                ) {
//                    Text(
//                        text = "broadcast transaction",
//                        textAlign = TextAlign.Center,
//                    )
//                }
//            }
//        }
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
            textStyle = TextStyle(fontFamily = ShareTechMono, color = md_theme_dark_onBackground),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = md_theme_dark_onBackgroundFaded,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Composable
private fun TransactionAmountInput(
    amount: MutableState<String>,
    transactionOption: TransactionType
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.9f),
            value = if (transactionOption == TransactionType.DEFAULT) amount.value else "${Wallet.getBalance()} (Before Fees)",
            onValueChange = { value: String ->
                amount.value = value.filter { it.isDigit() }
            },
            singleLine = true,
            textStyle = TextStyle(fontFamily = ShareTechMono, color = md_theme_dark_onBackground),
            label = {
                when (transactionOption) {
                    TransactionType.SEND_ALL -> {
                        Text(text = "Amount (Send All)")
                    }
                    else -> {
                        Text(text = "Amount (satoshis)")
                    }
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = md_theme_dark_onBackgroundFaded,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
            enabled = (
                when (transactionOption) {
                    TransactionType.SEND_ALL -> false
                    else -> true
                }
            )
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
            textStyle = TextStyle(fontFamily = ShareTechMono, color = md_theme_dark_onBackground),
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
    transactionOption: TransactionType,
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
                val confirmationText = when (transactionOption) {
                    TransactionType.DEFAULT -> "Send: $amount satoshis \nto: $recipientAddress\nFee rate: $feeRate"
                    TransactionType.SEND_ALL -> "Send: ${Wallet.getBalance()} satoshis \n" + "to: $recipientAddress\n" + "Fee rate: $feeRate"
                }
                Text(
                    text = confirmationText
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        broadcastTransaction(
                            recipientAddress = recipientAddress,
                            amount = amount,
                            feeRate = feeRate,
                            transactionOption = transactionOption,
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
    recipientAddress: String,
    amount: String,
    feeRate: String,
    transactionOption: TransactionType,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
): Boolean {
    if (recipientAddress.isBlank()) {
        scope.launch {
            snackbarHostState.showSnackbar(message = "Address is missing!", duration = SnackbarDuration.Short)
        }
        return false
    }
    if (transactionOption == TransactionType.DEFAULT && amount.isBlank()) {
        scope.launch {
            snackbarHostState.showSnackbar(message = "Amount is missing!", duration = SnackbarDuration.Short)
        }
        return false
    }
    if (feeRate.isBlank()) {
        scope.launch {
            snackbarHostState.showSnackbar(message = "Fee Rate is missing!", duration = SnackbarDuration.Short)
        }
        return false
    }
    if (feeRate.toInt() < 1 || feeRate.toInt() > 200) {
        scope.launch {
            snackbarHostState.showSnackbar(message = "Please input a fee rate between 1 and 200", duration = SnackbarDuration.Short)
        }
        return false
    }
    return true
}

private fun broadcastTransaction(
    recipientAddress: String,
    amount: String,
    feeRate: String,
    transactionOption: TransactionType,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
) {
    Log.i("SendScreen", "Attempting to broadcast transaction with inputs: recipient: ${recipientAddress}, amount: ${amount}, fee rate: $feeRate")
    var snackbarMsg: String
    try {
        // create, sign, and broadcast
        val psbt: PartiallySignedBitcoinTransaction = when (transactionOption) {
            TransactionType.DEFAULT -> Wallet.createTransaction(recipientAddress, amount.toULong(), feeRate.toFloat())
            TransactionType.SEND_ALL -> Wallet.createSendAllTransaction(recipientAddress, feeRate.toFloat())
        }
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

enum class TransactionType {
    DEFAULT,
    SEND_ALL,
}