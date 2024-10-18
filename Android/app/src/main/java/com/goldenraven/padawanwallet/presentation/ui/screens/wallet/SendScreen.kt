/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.wallet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.navigation.NavHostController
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.navigation.QRScanScreen
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_button_primary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_onPrimary
import com.goldenraven.padawanwallet.presentation.theme.gradientBackground
import com.goldenraven.padawanwallet.presentation.theme.innerScreenPadding
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_button_secondary_faded
import com.goldenraven.padawanwallet.presentation.theme.standardShadow
import com.goldenraven.padawanwallet.presentation.theme.wideTextField
import com.goldenraven.padawanwallet.presentation.ui.components.PadawanAppBar
import com.goldenraven.padawanwallet.presentation.ui.components.VerticalTextFieldDivider
import com.goldenraven.padawanwallet.presentation.ui.components.standardBorder
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.WalletAction
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.WalletState
import kotlinx.coroutines.launch
import org.rustbitcoin.bitcoin.Amount
import org.rustbitcoin.bitcoin.FeeRate
import org.bitcoindevkit.Transaction

private const val TAG = "SendScreen"

@Composable
internal fun SendScreen(
    state: WalletState,
    onAction: (WalletAction) -> Unit,
    paddingValues: PaddingValues,
    navController: NavHostController,
) {
    val recipientAddress: MutableState<String> = rememberSaveable { mutableStateOf("") }
    if (state.sendAddress != null) {
        recipientAddress.value = state.sendAddress
    }
    val amount: MutableState<String> = rememberSaveable { mutableStateOf("") }
    val feeRate: MutableState<Long> = rememberSaveable { mutableLongStateOf(1L) }

    val Peek = SheetDetent(identifier = "peek") { containerHeight, sheetHeight ->
        containerHeight * 0.6f
    }

    val sheetState = rememberModalBottomSheetState(
        initialDetent = Hidden,
        detents = listOf(Hidden, Peek, FullyExpanded)
    )
    val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
        ScreenSizeWidth.Small -> PaddingValues(12.dp)
        ScreenSizeWidth.Phone -> PaddingValues(32.dp)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            PadawanAppBar(
                title = stringResource(R.string.send_bitcoin),
                onClick = { navController.popBackStack() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { scaffoldPadding ->

            val scrollState = rememberScrollState()

            // TODO: Look into difference between consumeWindowInsets and simple padding
            Column(
                Modifier
                    .fillMaxSize()
                    .gradientBackground()
                    // .consumeWindowInsets(paddingValues)
                    .padding(scaffoldPadding)
                    .verticalScroll(scrollState)
                    .innerScreenPadding(padding)
            ) {
                Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
                        val balanceText = "${stringResource(id = R.string.balance)} ${state.balance} sat"

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
                    onValueChange = { value: String ->
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
                TextField(
                    modifier = Modifier
                    .wideTextField()
                    .height(IntrinsicSize.Min),
                    shape = RoundedCornerShape(20.dp),
                    value = recipientAddress.value,
                    onValueChange = { recipientAddress.value = it },
                    singleLine = true,
                    placeholder = { Text(text = stringResource(R.string.enter_a_bitcoin_signet_address)) },
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
                                    navController.navigate(QRScanScreen) {
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
                    }
                )

                Text(
                    text = stringResource(R.string.fees_sats_vbytes),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )

                var sliderPosition by remember { mutableFloatStateOf(1f) }
                Slider(
                    modifier = Modifier.semantics {
                        contentDescription = "Localized Description"
                    },
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 1f..8f,
                    onValueChangeFinished = {
                        feeRate.value = sliderPosition.toString().take(1).toLong()
                    },
                    steps = 6
                )
                Text(text = sliderPosition.toString().take(3))

                val showSnackbar: (String) -> Unit = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it,
                            duration = SnackbarDuration.Indefinite,
                            withDismissAction = true
                        )
                    }
                }
                val amountErrorMessage = stringResource(R.string.amount_error_message)
                val addressErrorMessage = stringResource(R.string.address_error_message)
                val feeRateErrorMessage = stringResource(R.string.fee_rate_error_message)

                Button(
                    onClick = {
                        var inputsAreValid = true
                        if (amount.value.isBlank()) {
                            showSnackbar(amountErrorMessage)
                            inputsAreValid = false
                        }
                        if (recipientAddress.value.isBlank()) {
                            showSnackbar(addressErrorMessage)
                            inputsAreValid = false
                        }
                        if (feeRate.value.toString().isBlank()) {
                            showSnackbar(feeRateErrorMessage)
                            inputsAreValid = false
                        }
                        try {
                            if (inputsAreValid) {
                                onAction(
                                    WalletAction.BuildAndSignPsbt(
                                        recipientAddress.value,
                                        Amount.fromSat(amount.value.toULong()),
                                        FeeRate.fromSatPerVb(feeRate.value.toULong())
                                    )
                                )
                                scope.launch { sheetState.currentDetent = Peek }
                            }
                        } catch (exception: Exception) {
                            Log.i(TAG, "Exception: $exception")
                            scope.launch {
                                showSnackbar("Error: ${exception.message}")
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
    )

    val showSnackbar: (String) -> Unit = {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
        }
    }

    ModalBottomSheet(state = sheetState) {
        Scrim()
        Sheet(
            modifier = Modifier
                .padding(top = 12.dp)
                .shadow(4.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color.White)
                .background(padawan_theme_button_secondary_faded)
                .widthIn(max = 640.dp)
                .fillMaxWidth()
                .imePadding(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1200.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                DragIndication(
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .background(Color.Black.copy(0.4f), RoundedCornerShape(100))
                        .width(32.dp)
                        .height(4.dp)
                )
                if (state.txAndFee != null) {
                    TransactionConfirmation(
                        state,
                        onAction,
                        state.txAndFee,
                        recipientAddress,
                        amount.value.toULong(),
                        showSnackbar,
                        navController,
                        { scope.launch { sheetState.currentDetent = Peek } }
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionConfirmation(
    state: WalletState,
    onAction: (WalletAction) -> Unit,
    txAndFee: Pair<Transaction, Amount>,
    recipientAddress: MutableState<String>,
    amount: ULong,
    showSnackbar: (String) -> Unit,
    navController: NavHostController,
    closeBottomSheet: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
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
                text = stringResource(id = R.string.amount),
                style = PadawanTypography.headlineSmall,
                fontSize = 20.sp,
            )
        }
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
        ) {
            val amountText = "$amount satoshis"
            Text(
                text = amountText,
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
                text = "${txAndFee.second.toSat()} satoshis",
                fontSize = 16.sp,
            )
        }

        val notOnlineMessage = stringResource(R.string.your_device_is_not_connected_to_the_internet)

        Button(
            onClick = {
                if (!state.isOnline) {
                    showSnackbar(notOnlineMessage)
                } else {
                    onAction(WalletAction.Broadcast(txAndFee.first))
                    closeBottomSheet()
                    navController.popBackStack()
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
                    text = stringResource(R.string.confirm_and_broadcast), color = Color(0xff000000)
                )
            }
        }
    }
}
