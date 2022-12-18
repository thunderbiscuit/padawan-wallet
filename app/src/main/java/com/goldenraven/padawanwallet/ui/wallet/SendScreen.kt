/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    val currencyList = listOf(CurrencyType.SATS, CurrencyType.BTC)
    var selectedCurrency by remember { mutableStateOf(0) }

    val qrCodeScanner = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("BTC_Address")?.observeAsState()
    qrCodeScanner?.value.let {
        if (it != null)
            recipientAddress.value = it

        navController.currentBackStackEntry?.savedStateHandle?.remove<String>("BTC_Address")
    }

    PadawanAppBar(navController = navController, title = "Send bitcoin")
    Column(modifier = Modifier.standardBackground()) {
        Row(modifier = Modifier.padding(top = 100.dp, bottom = 8.dp)) {
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
                    else -> true
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
                                    text = { Text(text = currency.toString().lowercase() ) },
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
