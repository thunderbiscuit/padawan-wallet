/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.screens.wallet

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.domain.wallet.Wallet
import com.goldenraven.padawanwallet.ui.theme.PadawanTypography
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_button_primary
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_button_secondary
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_onPrimary
import com.goldenraven.padawanwallet.ui.theme.gradientBackground
import com.goldenraven.padawanwallet.ui.theme.innerScreenPadding
import com.goldenraven.padawanwallet.ui.theme.standardShadow
import com.goldenraven.padawanwallet.ui.theme.wideTextField
import com.goldenraven.padawanwallet.ui.components.PadawanAppBar
import com.goldenraven.padawanwallet.utils.Screen
import com.goldenraven.padawanwallet.ui.components.VerticalTextFieldDivider
import com.goldenraven.padawanwallet.ui.components.standardBorder
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import com.goldenraven.padawanwallet.viewmodels.WalletViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bitcoindevkit.TxBuilderResult

private const val TAG = "SendScreen"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
internal fun SendScreen(navController: NavHostController, walletViewModel: WalletViewModel) {

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val balance by walletViewModel.balance.collectAsState()
    val recipientAddress: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val amount: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val feeRate: MutableState<String> = rememberSaveable { mutableStateOf("1") }
    val txBuilderResult: MutableState<TxBuilderResult?> = remember { mutableStateOf(null) }

    val qrCodeScanner = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>("BTC_Address")
        ?.observeAsState()

    qrCodeScanner?.value.let {
        if (it != null) recipientAddress.value = it
        navController.currentBackStackEntry?.savedStateHandle?.remove<String>("BTC_Address")
    }

    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetDragHandle = null,
        sheetContent = {
            TransactionConfirmation(
                txBuilderResult,
                recipientAddress,
                amount,
                bottomSheetScaffoldState,
                coroutineScope,
                navController,
                walletViewModel
            )
        },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
                ScreenSizeWidth.Small -> 12.dp
                ScreenSizeWidth.Phone -> 32.dp
            }

            val scrollState = rememberScrollState()

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .gradientBackground()
            ) {
                val (screenTitle, content) = createRefs()
                Row(Modifier.constrainAs(screenTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                    PadawanAppBar(navController = navController, title = stringResource(R.string.send_bitcoin))
                }

                Column(modifier = Modifier
                    .innerScreenPadding(padding)
                    .constrainAs(content) {
                        top.linkTo(screenTitle.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    }
                    .verticalScroll(scrollState)) {
                    Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
                        val balanceText = "${stringResource(id = R.string.balance)} $balance sat"

                        Text(
                            text = stringResource(R.string.amount),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .weight(weight = 0.5f)
                                .align(Alignment.Bottom)
                        )
                        Text(
                            text = balanceText,
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
                        value = amount.value,
                        onValueChange = { value ->
                            amount.value = value.filter { it.isDigit() }
                        },
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.enter_amount_sats)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = padawan_theme_background_secondary,
                            unfocusedContainerColor = padawan_theme_background_secondary,
                            disabledContainerColor = padawan_theme_background_secondary,
                            cursorColor = padawan_theme_onPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                        ),
                        enabled = (true),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                        ),
                    )

                    Text(
                        text = stringResource(id = R.string.address),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    TextField(modifier = Modifier
                        .wideTextField()
                        .height(IntrinsicSize.Min),
                        shape = RoundedCornerShape(20.dp),
                        value = recipientAddress.value,
                        onValueChange = { recipientAddress.value = it },
                        singleLine = true,
                        placeholder = { Text(text = stringResource(R.string.enter_a_bitcoin_testnet_address)) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = padawan_theme_background_secondary,
                            unfocusedContainerColor = padawan_theme_background_secondary,
                            disabledContainerColor = padawan_theme_background_secondary,
                            cursorColor = padawan_theme_onPrimary,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                        ),
                        trailingIcon = {
                            Row {
                                VerticalTextFieldDivider()
                                IconButton(
                                    onClick = {
                                        navController.navigate(Screen.QRScanScreen.route) {
                                            launchSingleTop = true
                                        }
                                    }, modifier = Modifier.align(Alignment.CenterVertically)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_camera),
                                        contentDescription = stringResource(R.string.scan_qr_icon),
                                    )
                                }
                            }
                        })

                    Text(
                        text = stringResource(R.string.fees_sats_vbytes),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    var sliderPosition by remember { mutableStateOf(1f) }
                    Slider(
                        modifier = Modifier.semantics {
                            contentDescription = "Localized Description"
                        },
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it },
                        valueRange = 1f..8f,
                        onValueChangeFinished = {
                            feeRate.value = sliderPosition.toString().take(3)
                        },
                        steps = 6
                    )
                    Text(text = sliderPosition.toString().take(3))
                    val amountErrorMessage = stringResource(R.string.amount_error_message)
                    val addressErrorMessage = stringResource(R.string.address_error_message)
                    val feeRateErrorMessage = stringResource(R.string.fee_rate_error_message)

                    Button(
                        onClick = {
                            val inputsAreValid = verifyInputs(
                                recipientAddress.value,
                                amount.value,
                                feeRate.value,
                                amountErrorMessage,
                                addressErrorMessage,
                                feeRateErrorMessage,
                                coroutineScope,
                                bottomSheetScaffoldState
                            )
                            try {
                                if (inputsAreValid) {
                                    val txBR = Wallet.createTransaction(
                                        recipientAddress.value,
                                        amount.value.toULong(),
                                        feeRate.value.toFloat()
                                    )
                                    txBuilderResult.value = txBR

                                    if (bottomSheetScaffoldState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded) {
                                        coroutineScope.launch {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                }
                            } catch (exception: Exception) {
                                Log.i(TAG, "Exception: $exception")
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.snackbarHostState.showSnackbar(
                                        message = "$exception",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                        shape = RoundedCornerShape(20.dp),
                        border = standardBorder,
                        modifier = Modifier
                            .padding(
                                top = 32.dp, start = 4.dp, end = 4.dp, bottom = 24.dp
                            )
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
                                text = stringResource(id = R.string.verify_transaction), color = Color(0xff000000)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionConfirmation(
    txBuilderResult: MutableState<TxBuilderResult?>,
    recipientAddress: MutableState<String>,
    amount: MutableState<String>,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    navController: NavHostController,
    viewModel: WalletViewModel,
) {
    val context = LocalContext.current // TODO: is this the right place to get this context?
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(padawan_theme_button_secondary)
            .border(
                3.dp,
                padawan_theme_button_primary,
                RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)
            )
            .padding(horizontal = 24.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.confirm_transaction),
                style = PadawanTypography.headlineLarge,
                fontSize = 24.sp,
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(id = R.string.amount_2),
                style = PadawanTypography.headlineSmall,
                fontSize = 20.sp,
            )
        }
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
        ) {
            val amountText = "${amount.value} satoshis"
            Text(
                text = amountText,
                // text = "${txBuilderResult.value?.transactionDetails?.sent ?: 0}",
                fontSize = 16.sp,
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(id = R.string.address),
                style = PadawanTypography.headlineSmall,
                fontSize = 20.sp,
            )
        }
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = recipientAddress.value,
                fontSize = 16.sp,
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Total fee",
                style = PadawanTypography.headlineSmall,
                fontSize = 20.sp,
            )
        }
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "${txBuilderResult.value?.transactionDetails?.fee ?: 0} satoshis",
                fontSize = 16.sp,
            )
        }

        val snackbarMessage = stringResource(R.string.your_device_is_not_connected_to_the_internet)
        val txBroadcastSuccess = stringResource(R.string.transaction_was_broadcast_successfully)

        Button(
            onClick = {
                val psbt = txBuilderResult.value?.psbt ?: throw Exception()
                viewModel.updateConnectivityStatus(context)
                if (!viewModel.isOnline.value) {
                    scope.launch {
                         scaffoldState.snackbarHostState.showSnackbar(
                            message = snackbarMessage,
                            duration = SnackbarDuration.Short
                        )
                    }
                } else {
                    viewModel.broadcastTransaction(
                        psbt,
                        scaffoldState.snackbarHostState,
                        txBroadcastSuccess
                    )
                    navController.popBackStack()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(
                    top = 32.dp, start = 4.dp, end = 4.dp, bottom = 24.dp
                )
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
                    text = stringResource(R.string.confirm_and_broadcast), color = Color(0xff000000)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun verifyInputs(
    recipientAddress: String,
    amount: String,
    feeRate: String,
    amountErrorMessage: String,
    addressErrorMessage: String,
    feeRateErrorMessage: String,
    scope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
): Boolean {
    if (amount.isBlank()) {
        scope.launch {
            bottomSheetScaffoldState.snackbarHostState.showSnackbar(
                message = amountErrorMessage,
                duration = SnackbarDuration.Short
            )
        }
        return false
    }
    if (recipientAddress.isBlank()) {
        scope.launch {
            bottomSheetScaffoldState.snackbarHostState.showSnackbar(
                message = addressErrorMessage,
                duration = SnackbarDuration.Short
            )
        }
        return false
    }
    if (feeRate.isBlank()) {
        scope.launch {
            bottomSheetScaffoldState.snackbarHostState.showSnackbar(
                message = feeRateErrorMessage,
                duration = SnackbarDuration.Short
            )
        }
        return false
    }
    // TODO: I don't think this is possible anymore given the slider.
    // if (feeRate.toFloat() < 1 || feeRate.toFloat() > 100) {
    //     scope.launch {
    //         bottomSheetScaffoldState.snackbarHostState.showSnackbar(
    //             message = "Please input a fee rate between 1 and 100",
    //             duration = SnackbarDuration.Short
    //         )
    //     }
    //     return false
    // }
    Log.i(TAG, "Inputs are valid")
    return true
}
