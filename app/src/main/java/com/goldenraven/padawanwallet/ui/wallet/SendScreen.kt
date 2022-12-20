/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bitcoindevkit.PartiallySignedBitcoinTransaction

private const val TAG = "SendScreen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun SendScreen(navController: NavHostController, walletViewModel: WalletViewModel) {
    val (showDialog, setShowDialog) = rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

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
    val transactionOption: MutableState<TransactionType> =
        rememberSaveable { mutableStateOf(transactionOptions[0]) }
    val showMenu: MutableState<Boolean> = remember { mutableStateOf(false) }
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    val currencyList = listOf(CurrencyType.SATS, CurrencyType.BTC)
    var selectedCurrency by remember { mutableStateOf(0) }

    val qrCodeScanner =
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("BTC_Address")
            ?.observeAsState()
    qrCodeScanner?.value.let {
        if (it != null)
            recipientAddress.value = it

        navController.currentBackStackEntry?.savedStateHandle?.remove<String>("BTC_Address")
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        sheetContent = { TransactionConfirmation() },
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = Color.White,
        sheetElevation = 12.dp,
        sheetPeekHeight = 0.dp,
        backgroundColor = padawan_theme_background
    ) {
        PadawanAppBar(navController = navController, title = "Send bitcoin")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .standardBackground()
        ) {
            Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
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
                modifier = Modifier
                    .wideTextField()
                    .height(IntrinsicSize.Min),
                shape = RoundedCornerShape(20.dp),
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
                        else                     -> true
                    }
                ),
                trailingIcon = {
                    Row {
                        VerticalTextFieldDivider()
                        Row(
                            Modifier
                                .noRippleClickable { dropDownMenuExpanded = true }
                                .fillMaxHeight()

                        ) {
                            Text(
                                text = currencyList[selectedCurrency].toString().lowercase(),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(bottom = 4.dp, start = 12.dp)
                                    .widthIn(min = 32.dp)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_dropdown),
                                contentDescription = "Dropdown Menu",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(end = 12.dp)
                            )
                            DropdownMenu(
                                expanded = dropDownMenuExpanded,
                                onDismissRequest = { dropDownMenuExpanded = false },
                                modifier = Modifier.background(Color.Transparent),
                            ) {
                                currencyList.forEachIndexed { index, currency ->
                                    DropdownMenuItem(
                                        onClick = { selectedCurrency = index },
                                        text = { Text(text = currency.toString().lowercase()) },
                                        colors = MenuDefaults.itemColors(
                                            textColor = if (selectedCurrency == index) padawan_theme_onPrimary else padawan_theme_onBackground_faded
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            )

            Text(
                text = "Address",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextField(
                modifier = Modifier
                    .wideTextField()
                    .height(IntrinsicSize.Min),
                shape = RoundedCornerShape(20.dp),
                value = recipientAddress.value,
                onValueChange = { recipientAddress.value = it },
                singleLine = true,
                placeholder = { Text(text = "Enter a bitcoin testnet address") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = padawan_theme_background_secondary,
                    cursorColor = padawan_theme_onPrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                trailingIcon = {
                    Row {
                        VerticalTextFieldDivider()
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.QRScanScreen.route) {
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_camera),
                                contentDescription = "Scan QR Icon",
                            )
                        }
                    }
                }
            )

            Text(
                text = "Fees",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextField(
                modifier = Modifier
                    .wideTextField()
                    .height(IntrinsicSize.Min),
                shape = RoundedCornerShape(20.dp),
                value = feeRate.value,
                onValueChange = { value -> feeRate.value = value.filter { it.isDigit() } },
                singleLine = true,
                placeholder = { Text(text = "Edit fees") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = padawan_theme_background_secondary,
                    cursorColor = padawan_theme_onPrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                )
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(top = 32.dp, start = 4.dp, end = 4.dp, bottom = 24.dp)
                    .standardShadow(20.dp)
                    .height(70.dp)
                    .width(240.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Verify transaction",
                        color = Color(0xff000000)
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionConfirmation() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        )
        {
            Text(
                text = "Confirm Transaction",
                fontSize = 24.sp,
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Amount",
                fontSize = 20.sp,
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "my amount",
                fontSize = 16.sp,
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Address",
                fontSize = 20.sp,
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "my address",
                fontSize = 16.sp,
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Fee",
                fontSize = 20.sp,
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "fee",
                fontSize = 16.sp,
            )
        }

        Button(
            onClick = {
                // Wallet.broadcast()
            },
            colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(top = 32.dp, start = 4.dp, end = 4.dp, bottom = 24.dp)
                .standardShadow(20.dp)
                .height(70.dp)
                .width(240.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Confirm and broadcast",
                    color = Color(0xff000000)
                )
            }
        }
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
    Log.i(TAG, "Attempting to broadcast transaction with inputs: recipient: ${recipientAddress}, amount: ${amount}, fee rate: $feeRate")
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
        Log.i(TAG, "Transaction was broadcast! txid: $txid")
    } catch (e: Throwable) {
        Log.i(TAG, "Broadcast error: ${e.message}")
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
