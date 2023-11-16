/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

import android.content.Context
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.tx.Tx
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.FadedVerticalDivider
import com.goldenraven.padawanwallet.ui.LoadingAnimation
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.standardBorder
import com.goldenraven.padawanwallet.utils.ClickHelper
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.formatCurrency
import com.goldenraven.padawanwallet.utils.formatInBtc
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth

// TODO Think about reintroducing refreshing
// TODO Reuse composable more
// TODO Handle no internet connection situations
// TODO Handle old faucet dialog
// TODO Finish up send & receive screen

private const val TAG = "WalletRootScreen"

@Composable
internal fun WalletRootScreen(
    navController: NavHostController,
    walletViewModel: WalletViewModel
) {
    val balance by walletViewModel.balance.collectAsState()
    val transactionList by walletViewModel.readAllData.collectAsState(initial = emptyList())
    val isOnlineStatus by walletViewModel.isOnlineVariable.collectAsState()
    val tempOpenFaucetDialog = walletViewModel.openFaucetDialog
    val context = LocalContext.current

    if (tempOpenFaucetDialog.value) {
        FaucetDialog(
            walletViewModel = walletViewModel
        )
    }

    val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
        ScreenSizeWidth.Small -> 12.dp
        ScreenSizeWidth.Phone -> 32.dp
    }

    Column(
        modifier = Modifier.gradientBackground().innerScreenPadding(padding)
    ) {
        // This text composable uses the shared code from the shared module
        // Text(
        //     modifier = Modifier.align(Alignment.CenterHorizontally),
        //     text = Greeting().greet()
        // )
        if (isOnlineStatus == false) { NoNetworkBanner(walletViewModel, context) }
        BalanceBox(balance = balance ?: 0uL, viewModel = walletViewModel)
        Spacer(modifier = Modifier.height(height = 12.dp))
        SendReceive(navController = navController)
        TransactionListBox(tempOpenFaucetDialog = tempOpenFaucetDialog, transactionList = transactionList, navController = navController)
    }
}

@Composable
fun NoNetworkBanner(walletViewModel: WalletViewModel, context: Context) {
    val screenSizeWidth = getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)
    val fontSize = when (screenSizeWidth) {
        ScreenSizeWidth.Small -> 12.sp
        ScreenSizeWidth.Phone -> 16.sp
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .height(40.dp)
            .clickable {
                walletViewModel.updateConnectivityStatus(context)
            },
        border = standardBorder,
        colors = CardDefaults.cardColors(Color(0xfff6cf47)),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.currently_unable_to_access_network),
                fontSize = fontSize
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceBox(
    balance: ULong,
    viewModel: WalletViewModel
) {
    val context = LocalContext.current // TODO #4: Is this the right place to get this context?
    Card(
        border = standardBorder,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(padawan_theme_onBackground_secondary),
        modifier = Modifier
            .standardShadow(20.dp)
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 0.dp)
                .fillMaxWidth()
        ) {
            val (cardName, currencyToggle, balanceText, currencyText, buttonRow) = createRefs()
            val currencyToggleState = remember { mutableStateOf(value = true) }
            Text(
                text = stringResource(R.string.bitcoin_testnet),
                style = PadawanTypography.bodyMedium,
                color = padawan_theme_text_faded,
                modifier = Modifier.constrainAs(cardName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )
            Box(
                modifier = Modifier
                    .noRippleClickable {
                        currencyToggleState.value = !currencyToggleState.value
                    }
                    .background(
                        color = padawan_theme_button_secondary,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .constrainAs(currencyToggle) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            ) {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = 8.dp)
                ) {
                    CurrencyToggleText(
                        currencyToggleState = currencyToggleState,
                        text = CurrencyType.BTC
                    )
                    FadedVerticalDivider()
                    CurrencyToggleText(
                        currencyToggleState = currencyToggleState,
                        text = CurrencyType.SATS
                    )
                }
            }
            var balanceDisplay: String = if (currencyToggleState.value) balance.toString() else balance.formatInBtc()
            balanceDisplay = formatCurrency(balanceDisplay)
            val currencyDisplay: String = if (currencyToggleState.value) {
                CurrencyType.SATS.toString().lowercase()
            } else {
                CurrencyType.BTC.toString().lowercase()
            }
            val fontSize = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
                ScreenSizeWidth.Small -> 28.sp
                ScreenSizeWidth.Phone -> 36.sp
            }

            Text(
                text = balanceDisplay,
                style = PadawanTypography.displaySmall,
                fontSize = fontSize,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .constrainAs(balanceText) {
                        top.linkTo(cardName.bottom)
                        start.linkTo(parent.start)
                    }
            )
            Text(
                text = currencyDisplay,
                style = PadawanTypography.bodyMedium,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .constrainAs(currencyText) {
                        start.linkTo(balanceText.end)
                        bottom.linkTo(balanceText.bottom)
                    }
            )
            Row(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 0.dp)
                    .constrainAs(buttonRow) {
                        top.linkTo(balanceText.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                val isRefreshing by viewModel.isRefreshing.collectAsState()
                CompositionLocalProvider(
                    LocalMinimumTouchTargetEnforcement provides false,
                ) {
                    Button(
                        onClick = {
                            viewModel.updateConnectivityStatus(context)
                            viewModel.refresh(context)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            disabledContainerColor = Color.Black
                        ),
                        shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
                        border = standardBorder,
                        modifier = Modifier.width(134.dp),
                        enabled = !isRefreshing
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (isRefreshing) {
                                LoadingAnimation()
                            } else {
                                Text(
                                    text = stringResource(id = R.string.sync),
                                    style = PadawanTypography.labelLarge,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xffdbdeff),
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SendReceive(navController: NavHostController) {
    val screenSizeWidth: ScreenSizeWidth = getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)

    Row(
        modifier = Modifier
            .padding(top = 4.dp)
            .height(70.dp)
    ) {
        Button(
            onClick = { ClickHelper.clickOnce { navController.navigate(Screen.ReceiveScreen.route) }},
            colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_secondary),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(all = 4.dp)
                .standardShadow(20.dp)
                .weight(weight = 0.5f)
                .fillMaxHeight()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = stringResource(id = R.string.receive),
                    style = PadawanTypography.labelLarge,
                )
                if (screenSizeWidth == ScreenSizeWidth.Phone) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_receive),
                        contentDescription = stringResource(id = R.string.receive_icon)
                    )
                }
            }
        }
        Button(
            onClick = { ClickHelper.clickOnce { navController.navigate(Screen.SendScreen.route) }},
            colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(all = 4.dp)
                .standardShadow(20.dp)
                .weight(weight = 0.5f)
                .fillMaxHeight(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = stringResource(id = R.string.send),
                    style = PadawanTypography.labelLarge,
                )
                if (screenSizeWidth == ScreenSizeWidth.Phone) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send,),
                        contentDescription = stringResource(id = R.string.send_icon),
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionListBox(
    tempOpenFaucetDialog: MutableState<Boolean>,
    transactionList: List<Tx>,
    navController: NavHostController,
) {
    Row(modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)) {
        Text(
            text = stringResource(id = R.string.transactions),
            style = PadawanTypography.headlineSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.Bottom)
                .weight(weight = 0.5f)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = standardBorder,
        shape = RoundedCornerShape(20.dp),
        // containerColor = padawan_theme_background_secondary,
        colors = CardDefaults.cardColors(padawan_theme_background_secondary),
    ) {
        val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
            ScreenSizeWidth.Small -> 12.dp
            ScreenSizeWidth.Phone -> 24.dp
        }

        if (transactionList.isEmpty()) {
            Row(modifier = Modifier.padding(all = padding)) {
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier.verticalScroll(state = scrollState)
                ) {
                    Text(
                        text = stringResource(R.string.looks_like_your_transaction_list_is_empty),
                        style = PadawanTypography.bodyMedium,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                    Button(
                        onClick = { tempOpenFaucetDialog.value = true },
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .standardShadow(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                        shape = RoundedCornerShape(20.dp),
                        border = standardBorder
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(text = stringResource(R.string.get_coins), style = PadawanTypography.bodyMedium)
                            Icon(
                                painter = painterResource(id = R.drawable.ic_receive_secondary),
                                contentDescription = stringResource(R.string.get_coins_icon)
                            )
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(color = padawan_theme_lazyColumn_background)
                    .padding(horizontal = 24.dp)
            ) {
                itemsIndexed(transactionList) { index, tx ->
                    if (index == 0) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    Column(
                        modifier = Modifier.noRippleClickable { viewTransaction(navController, txid = tx.txid) }
                    ) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                        ) {
                            Text(
                                text = "${tx.txid.take(n = 5)}.....${tx.txid.takeLast(n = 5)}",
                                style = PadawanTypography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                // fontFamily = ShareTechMono,
                                maxLines = 1,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(top = 8.dp)
                            )
                            Text(
                                text = "${if (tx.isPayment) tx.valueOut.toString() else tx.valueIn.toString()} ${CurrencyType.SATS.toString().lowercase()}",
                                style = PadawanTypography.bodyMedium,
                                textAlign = TextAlign.End,
                                modifier = Modifier.align(Alignment.BottomEnd)
                            )
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)) {
                            // val message = if (tx.date == "Pending") "Pending" else "${getDateDifference(date = tx.date)} ago"
                            Text(
                                text = tx.date,
                                style = PadawanTypography.bodySmall,
                                maxLines = 1,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .background(
                                            color = if (tx.isPayment) padawan_theme_send_primary else padawan_theme_receive_primary,
                                            shape = RoundedCornerShape(size = 5.dp)
                                        )
                                ) {
                                    Text(
                                        text = if (tx.isPayment) stringResource(id = R.string.sent) else stringResource(id = R.string.received),
                                        style = PadawanTypography.bodySmall,
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                                    )
                                    Icon(
                                        painter = if (tx.isPayment) painterResource(id = R.drawable.ic_send_secondary) else painterResource(id = R.drawable.ic_receive_secondary),
                                        tint = padawan_disabled,
                                        contentDescription = if (tx.isPayment) stringResource(id = R.string.send_icon) else stringResource(id = R.string.receive_icon),
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .scale(scale = 0.75f)
                                            .padding(end = 8.dp),
                                    )
                                }
                            }
                        }
                        if (index != transactionList.size - 1) {
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        } else {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyToggleText(currencyToggleState: MutableState<Boolean>, text: CurrencyType) {
    val currencyState = (!currencyToggleState.value && text == CurrencyType.BTC) || (currencyToggleState.value && text == CurrencyType.SATS)

    val colorTransition = updateTransition(
        targetState = if (currencyState) padawan_theme_onBackground_faded else padawan_theme_onPrimary,
        label = stringResource(R.string.currency_toggle_text)
    )
    val color by colorTransition.animateColor(
        transitionSpec = { tween(durationMillis = 500) },
        label = stringResource(R.string.changing_color_animation),
    ) {
        if (it == padawan_theme_onBackground_faded) padawan_theme_onPrimary else padawan_theme_onBackground_faded
    }

    Text(
        text = text.toString().lowercase(),
        textAlign = TextAlign.Center,
        style = PadawanTypography.bodyMedium,
        color = color,
        modifier = Modifier.padding(all = 8.dp),
    )
}

@Composable
private fun FaucetDialog(walletViewModel: WalletViewModel) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = stringResource(R.string.hello_there),
                style = PadawanTypography.headlineMedium,
                color = padawan_theme_text_headline
            )
        },
        text = {
            Text(
                text = stringResource(R.string.would_you_like_to_receive_some_testnet_bitcoin),
                fontSize = 18.sp,
                lineHeight = 24.sp,
                color = padawan_theme_text_faded_secondary
            )
        },

        dismissButton = {
            Button(
                onClick = {
                    walletViewModel.onNegativeDialogClick()
                    walletViewModel.openFaucetDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xfffc4f4f)),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
                    .standardShadow(20.dp)
                    .height(70.dp)
                    .width(110.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    // Text(
                    //     text = "Not now",
                    //     style = PadawanTypography.labelMedium,
                    //     fontSize = 12.sp,
                    //     color = Color(0xff000000)
                    // )
                    // Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hicon_dislike),
                        contentDescription = stringResource(R.string.no_thank_you_icon),
                        tint = Color(0xff000000)
                    )
                }
            }
        },

        confirmButton = {
            Button(
                onClick = {
                    walletViewModel.onPositiveDialogClick()
                    walletViewModel.openFaucetDialog.value = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_background),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(0.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
                    .standardShadow(20.dp)
                    .height(70.dp)
                    .width(110.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    // Text(
                    //     text = "Yes please!",
                    //     style = PadawanTypography.labelMedium,
                    //     fontSize = 12.sp,
                    //     color = Color(0xff000000)
                    // )
                    // Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_hicon_like),
                        contentDescription = stringResource(R.string.proceed_icon),
                        tint = Color(0xff000000)
                    )
                }
            }
        },
    )
}

private fun viewTransaction(navController: NavController, txid: String) {
    navController.navigate("${Screen.TransactionScreen.route}/txid=$txid")
}

@Preview(name = "PIXEL 4", device = Devices.PIXEL_4, showBackground = true)
@Preview(name = "PIXEL 2, 270 Wide", device = Devices.PIXEL_2, widthDp = 270)
@Composable
internal fun PreviewSendReceiveRow() {
    PadawanTheme {
        SendReceive(
            rememberNavController(),
        )
    }
}
