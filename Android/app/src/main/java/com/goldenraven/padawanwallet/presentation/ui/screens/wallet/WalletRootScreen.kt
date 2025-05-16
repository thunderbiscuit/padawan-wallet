/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.wallet

import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.ArrowDownToLine
import com.composables.icons.lucide.ArrowUpFromLine
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ThumbsDown
import com.composables.icons.lucide.ThumbsUp
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.navigation.SendScreen
import com.goldenraven.padawanwallet.presentation.navigation.ReceiveScreen
import com.goldenraven.padawanwallet.presentation.navigation.TransactionScreen
import com.goldenraven.padawanwallet.domain.bitcoin.BitcoinUnit
import com.goldenraven.padawanwallet.domain.bitcoin.ChainPosition
import com.goldenraven.padawanwallet.domain.bitcoin.TransactionDetails
import com.goldenraven.padawanwallet.presentation.ui.components.LoadingAnimation
import com.goldenraven.padawanwallet.presentation.ui.components.standardBorder
import com.goldenraven.padawanwallet.presentation.theme.PadawanTheme
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.innerScreenPadding
import com.goldenraven.padawanwallet.presentation.theme.noRippleClickable
import com.goldenraven.padawanwallet.presentation.theme.PadawanColorsTatooineDesert
import com.goldenraven.padawanwallet.presentation.theme.standardShadow
import com.goldenraven.padawanwallet.utils.ClickHelper
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.formatCurrency
import com.goldenraven.padawanwallet.utils.formatInBtc
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.WalletAction
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.WalletState
import com.goldenraven.padawanwallet.domain.bitcoin.TxType
import com.goldenraven.padawanwallet.presentation.theme.LocalPadawanColors
import com.goldenraven.padawanwallet.utils.timestampToString

private const val TAG = "WalletRootScreen"

@Composable
internal fun WalletRootScreen(
    state: WalletState,
    onAction: (WalletAction) -> Unit,
    paddingValues: PaddingValues,
    navController: NavHostController,
) {
    val colors = LocalPadawanColors.current
    val (openDialog, setOpenDialog) = remember { mutableStateOf(false) }
    if (openDialog) FaucetDialog(onAction, setOpenDialog)

    val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
        ScreenSizeWidth.Small -> PaddingValues(horizontal = 12.dp)
        ScreenSizeWidth.Phone -> PaddingValues(start = 32.dp, top = 12.dp, end = 32.dp, bottom = 0.dp)
    }

    if (state.messageForUi != null) {
        Toast.makeText(
            LocalContext.current,
            state.messageForUi.second,
            Toast.LENGTH_LONG
        ).show()
        onAction(WalletAction.UiMessageDelivered)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(paddingValues)
            .innerScreenPadding(padding)
    ) {
        if (!state.isOnline) { NoNetworkBanner(onAction) }
        BalanceBox(balance = state.balance, currentlySyncing = state.currentlySyncing, onAction = onAction)
        Spacer(modifier = Modifier.height(height = 12.dp))
        SendReceive(navController, state.isOnline)
        TransactionListBox(
            setOpenDialog = setOpenDialog,
            transactionList = state.transactions,
            isOnline = state.isOnline,
            onAction = onAction,
            navController = navController,
        )
    }
}

@Composable
fun NoNetworkBanner(onAction: (WalletAction) -> Unit) {
    val colors = LocalPadawanColors.current
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
            .clickable { onAction(WalletAction.CheckNetworkStatus) },
        border = standardBorder,
        colors = CardDefaults.cardColors(colors.accent2),
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
    currentlySyncing: Boolean,
    onAction: (WalletAction) -> Unit,
) {
    val colors = LocalPadawanColors.current

    Card(
        border = standardBorder,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(colors.accent1),
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
            var currencyToggleState by remember { mutableStateOf(true) }
            Text(
                text = stringResource(R.string.bitcoin_signet),
                style = PadawanTypography.bodyMedium,
                color = Color(0x771a1a1a), // Standard text color with some transparency
                modifier = Modifier.constrainAs(cardName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )
            Box(
                modifier = Modifier
                    .noRippleClickable {
                        currencyToggleState = !currencyToggleState
                    }
                    // .border(width = 2.dp, color = PadawanColors.text)
                    .background(
                        shape = RoundedCornerShape(size = 10.dp),
                        color = colors.accent1Light,
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
                        text = BitcoinUnit.BTC
                    )
                    CurrencyToggleText(
                        currencyToggleState = currencyToggleState,
                        text = BitcoinUnit.SATS
                    )
                }
            }
            var balanceDisplay: String = if (currencyToggleState) balance.toString() else balance.formatInBtc()
            balanceDisplay = formatCurrency(balanceDisplay)
            val currencyDisplay: String = if (currencyToggleState) {
                BitcoinUnit.SATS.toString().lowercase()
            } else {
                BitcoinUnit.BTC.toString().lowercase()
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
                CompositionLocalProvider(
                    LocalMinimumInteractiveComponentEnforcement provides false,
                ) {
                    Button(
                        onClick = { onAction(WalletAction.Sync) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            disabledContainerColor = Color.Black
                        ),
                        shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
                        border = standardBorder,
                        modifier = Modifier.width(134.dp),
                        enabled = !currentlySyncing
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (currentlySyncing) {
                                LoadingAnimation(circleColor = colors.accent2)
                            } else {
                                Text(
                                    text = stringResource(id = R.string.sync),
                                    style = PadawanTypography.labelLarge,
                                    fontWeight = FontWeight.Normal,
                                    color = colors.background,
                                    // color = Color.White,
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
fun SendReceive(navController: NavHostController, isOnline: Boolean) {
    val screenSizeWidth: ScreenSizeWidth = getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)

    Row(
        modifier = Modifier
            .padding(top = 4.dp)
            .height(70.dp)
    ) {
        Button(
            onClick = { ClickHelper.clickOnce { navController.navigate(ReceiveScreen) }},
            colors = ButtonDefaults.buttonColors(containerColor = PadawanColorsTatooineDesert.accent2),
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
                    Image(
                        imageVector = Lucide.ArrowDownToLine,
                        contentDescription  = stringResource(id = R.string.receive_icon),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        Button(
            onClick = { ClickHelper.clickOnce { navController.navigate(SendScreen) }},
            colors = ButtonDefaults.buttonColors(
                containerColor = PadawanColorsTatooineDesert.accent2,
                disabledContainerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            enabled = isOnline,
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
                    Image(
                        imageVector = Lucide.ArrowUpFromLine,
                        contentDescription = stringResource(id = R.string.send_icon),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionListBox(
    setOpenDialog: (Boolean) -> Unit,
    transactionList: List<TransactionDetails>,
    isOnline: Boolean,
    onAction: (WalletAction) -> Unit,
    navController: NavHostController,
) {
    val colors = LocalPadawanColors.current

    Row(modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)) {
        Text(
            text = stringResource(id = R.string.transactions),
            style = PadawanTypography.headlineSmall,
            color = PadawanColorsTatooineDesert.text,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.Bottom)
                .weight(weight = 0.5f)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth().standardShadow(20.dp),
        border = standardBorder,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(PadawanColorsTatooineDesert.background),
    ) {
        val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
            ScreenSizeWidth.Small -> 12.dp
            ScreenSizeWidth.Phone -> 24.dp
        }

        if (transactionList.isEmpty()) {
            Row(
                modifier = Modifier.padding(all = padding).background(PadawanColorsTatooineDesert.background2)
            ) {
                val scrollState = rememberScrollState()

                Column(
                    modifier = Modifier.verticalScroll(state = scrollState).background(PadawanColorsTatooineDesert.background)
                ) {
                    Text(
                        text = stringResource(R.string.looks_like_your_transaction_list_is_empty),
                        style = PadawanTypography.bodyMedium,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                    Button(
                        onClick = { setOpenDialog(true) },
                        enabled = isOnline,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .standardShadow(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PadawanColorsTatooineDesert.accent2,
                            disabledContainerColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp),
                        border = standardBorder
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(text = stringResource(R.string.get_coins), style = PadawanTypography.bodyMedium)
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(color = PadawanColorsTatooineDesert.background2)
                    .padding(horizontal = 24.dp)
            ) {
                itemsIndexed(transactionList) { index, tx ->
                    if (index == 0) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    Column(
                        modifier = Modifier.noRippleClickable {
                            onAction(WalletAction.SeeSingleTx(tx.txid))
                            navController.navigate(TransactionScreen)
                        }
                    ) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                        ) {
                            Text(
                                text = "${tx.txid.take(n = 5)}.....${tx.txid.takeLast(n = 5)}",
                                style = PadawanTypography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(top = 8.dp)
                            )
                            Text(
                                text = "${if (tx.txType == TxType.OUTBOUND) tx.paymentAmount.toString() else tx.received.toSat()} ${BitcoinUnit.SATS.toString().lowercase()}",
                                style = PadawanTypography.bodyMedium,
                                textAlign = TextAlign.End,
                                modifier = Modifier.align(Alignment.BottomEnd)
                            )
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                        ) {
                            val confirmationText: String = when (tx.chainPosition) {
                                is ChainPosition.Unconfirmed -> stringResource(id = R.string.pending)
                                is ChainPosition.Confirmed -> tx.chainPosition.timestamp.timestampToString()
                            }
                            Text(
                                text = confirmationText,
                                style = PadawanTypography.bodySmall,
                                maxLines = 1,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                            Box(modifier = Modifier.align(Alignment.BottomEnd)) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .background(
                                            color = Color(0xFFEDE0C4),
                                            // color = if (tx.txType == TxType.OUTBOUND) PadawanColors.sendPrimary else PadawanColors.receivePrimary,
                                            shape = RoundedCornerShape(size = 5.dp)
                                        )
                                ) {
                                    Text(
                                        text = if (tx.txType == TxType.OUTBOUND) stringResource(id = R.string.send) else stringResource(id = R.string.receive),
                                        style = PadawanTypography.bodySmall,
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                                    )
                                    Icon(
                                        imageVector = if (tx.txType == TxType.OUTBOUND) Lucide.ArrowUpFromLine else Lucide.ArrowDownToLine,
                                        tint = colors.textFaded,
                                        contentDescription = if (tx.txType == TxType.OUTBOUND) stringResource(id = R.string.send_icon) else stringResource(id = R.string.receive_icon),
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .scale(scale = 0.75f)
                                            .padding(end = 8.dp),
                                    )
                                }
                            }
                        }
                        if (index != transactionList.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
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
fun CurrencyToggleText(currencyToggleState: Boolean, text: BitcoinUnit) {
    val colors = LocalPadawanColors.current
    val currencyState = (!currencyToggleState && text == BitcoinUnit.BTC) || (currencyToggleState && text == BitcoinUnit.SATS)

    val colorTransition = updateTransition(
        targetState = if (currencyState) colors.textFaded else colors.text,
        label = stringResource(R.string.currency_toggle_text)
    )
    val color by colorTransition.animateColor(
        transitionSpec = { tween(durationMillis = 500) },
        label = stringResource(R.string.changing_color_animation),
    ) {
        if (it == colors.textFaded) colors.text else colors.textFaded
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
private fun FaucetDialog(
    onAction: (WalletAction) -> Unit,
    setOpenDialog: (Boolean) -> Unit,
) {
    val colors = LocalPadawanColors.current

    AlertDialog(
        containerColor = colors.background2,
        onDismissRequest = {},
        title = {
            Text(
                text = stringResource(R.string.hello_there),
                style = PadawanTypography.headlineMedium,
                color = colors.text
            )
        },
        text = {
            Text(
                text = stringResource(R.string.would_you_like_to_receive_some_signet_bitcoin),
                fontSize = 18.sp,
                lineHeight = 24.sp,
                color = colors.textLight
            )
        },

        dismissButton = {
            Button(
                onClick = { setOpenDialog(false) },
                colors = ButtonDefaults.buttonColors(containerColor = colors.errorRed),
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
                    Icon(
                        imageVector = Lucide.ThumbsDown,
                        contentDescription = stringResource(R.string.no_thank_you_icon),
                        tint = Color(0xff000000)
                    )
                }
            }
        },

        confirmButton = {
            Button(
                onClick = {
                    setOpenDialog(false)
                    onAction(WalletAction.RequestCoins)
                },
                colors = ButtonDefaults.buttonColors(containerColor = colors.goGreen),
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
                    Icon(
                        imageVector = Lucide.ThumbsUp,
                        contentDescription = stringResource(R.string.proceed_icon),
                        tint = Color(0xff000000)
                    )
                }
            }
        },
    )
}

@Preview(name = "PIXEL 7", device = Devices.PIXEL_7, showBackground = true)
@Preview(name = "PIXEL 2, 270 Wide", device = Devices.PIXEL_2, widthDp = 270)
@Composable
internal fun PreviewSendReceiveRow() {
    PadawanTheme {
        SendReceive(
            rememberNavController(),
            isOnline = true
        )
    }
}
