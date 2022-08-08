/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Tx
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.FadedVerticalDivider
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.standardBorder
import com.goldenraven.padawanwallet.utils.getDateDifference

@Composable
internal fun WalletScreen(navController: NavHostController, walletViewModel: WalletViewModel, ) {
    val balance by walletViewModel.balance.observeAsState()
    val isRefreshing by walletViewModel.isRefreshing.collectAsState()
    val transactionList by walletViewModel.readAllData.observeAsState(initial = emptyList())
    val openFaucetDialog = walletViewModel.openFaucetDialog
    val context = LocalContext.current

    if (openFaucetDialog.value) {
        FaucetDialog(
            walletViewModel = walletViewModel
        )
    }

    if (walletViewModel.isOnline(context = context) && !Wallet.isBlockChainCreated()) {
        Wallet.createBlockchain()
    }

    Column(modifier = Modifier.standardBackground()) {
        MainBox(navController = navController, balance = balance.toString())
        TransactionListBox(openFaucetDialog = openFaucetDialog, transactionList = transactionList)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainBox(navController: NavHostController, balance: String) {
    Card(
        border = standardBorder,
        shape = RoundedCornerShape(20.dp),
        containerColor = padawan_theme_onBackground_secondary,
        modifier = Modifier
            .standardShadow()
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth()
        ) {
            val (cardName, currencyToggle, balanceText, currencyText, buttonRow) = createRefs()
            val currencyToggleState = remember { mutableStateOf(value = false) }
            Text(
                text = "bitcoin testnet",
                modifier = Modifier.constrainAs(cardName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )
            Box(
                modifier = Modifier
                    .constrainAs(currencyToggle) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .background(
                        color = padawan_theme_button_secondary,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .noRippleClickable { currencyToggleState.value = !currencyToggleState.value }
            ) {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = 8.dp)
                ) {
                    CurrencyToggleText(currencyToggleState = currencyToggleState, text = CurrencyType.BTC)
                    FadedVerticalDivider()
                    CurrencyToggleText(currencyToggleState = currencyToggleState, text = CurrencyType.SATS)
                }
            }
            Text(
                text = balance,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .constrainAs(balanceText) {
                    top.linkTo(cardName.bottom)
                    start.linkTo(parent.start)
                }
            )
            Text(
                text = CurrencyType.SATS.toString().lowercase(),
                modifier = Modifier
                    .padding(all = 8.dp)
                    .constrainAs(currencyText) {
                        start.linkTo(balanceText.end)
                        bottom.linkTo(balanceText.bottom)
                    }
            )
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .constrainAs(buttonRow) {
                        top.linkTo(balanceText.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Button(
                    onClick = { navController.navigate(Screen.ReceiveScreen.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_secondary),
                    shape = RoundedCornerShape(20.dp),
                    border = standardBorder,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .standardShadow()
                        .weight(weight = 0.5f)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = "Receive",
                            style = PadawanTypography.labelLarge,
                        )
                        Icon(painter = painterResource(id = R.drawable.ic_receive), contentDescription = "Receive Icon")
                    }
                }
                Button(
                    onClick = { navController.navigate(Screen.SendScreen.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                    shape = RoundedCornerShape(20.dp),
                    border = standardBorder,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .standardShadow()
                        .weight(weight = 0.5f),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = "Send",
                            style = PadawanTypography.labelLarge,
                        )
                        Icon(painter = painterResource(id = R.drawable.ic_send), contentDescription = "Send Icon")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListBox(openFaucetDialog: MutableState<Boolean>, transactionList: List<Tx>) {
    Row(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
        Text(
            text = "Transactions",
            style = PadawanTypography.headlineSmall,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .weight(weight = 0.5f)
                .align(Alignment.Bottom)
        )
        Text(
            text = "View all transactions",
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(weight = 0.5f)
                .align(Alignment.Bottom)
                .clickable { /* TODO */ }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = standardBorder,
        shape = RoundedCornerShape(20.dp),
        containerColor = padawan_theme_background_secondary
    ) {
        if (transactionList.isEmpty()) {
            Row(modifier = Modifier.padding(all = 32.dp)) {
                Column(modifier = Modifier.weight(weight = 0.70f)) {
                    Text(
                        text = "Hey! Your transaction list is empty, get some coins so you can fill it up.",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(all = 8.dp)
                    )
                    Button(
                        onClick = { openFaucetDialog.value = true },
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .standardShadow(),
                        colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                        shape = RoundedCornerShape(20.dp),
                        border = standardBorder
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(text = "Get coins")
                            Icon(painter = painterResource(id = R.drawable.ic_receive), contentDescription = "Get Coins Icon")
                        }
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.diagram_wip),
                    contentDescription = "No Transactions Diagram",
                    modifier = Modifier
                        .weight(weight = 0.30f)
                        .align(Alignment.CenterVertically)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(color = padawan_theme_lazyColumn_background)
                    .padding(horizontal = 24.dp)
            ) {
                itemsIndexed(transactionList) { index, txn ->
                    if (index == 0) {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    Column {
                        Row {
                            Text(
                                text = "${txn.txid.take(n = 5)}.....${txn.txid.takeLast(n = 5)}",
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(weight = 0.5f)
                                    .align(Alignment.Bottom)
                            )
                            Text(
                                text = "${if (txn.isPayment) txn.valueOut.toString() else txn.valueIn.toString()} ${CurrencyType.SATS.toString().lowercase()}",
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .weight(weight = 0.5f)
                                    .align(Alignment.Bottom)
                            )
                        }
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "${getDateDifference(date = txn.date)} ago",
                                textAlign = TextAlign.Start,
                                fontSize = 14.sp,
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(weight = 0.5f)
                                    .align(Alignment.Bottom)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(weight = 0.5f)
                                    .align(Alignment.Bottom)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .background(
                                            color = if (txn.isPayment) padawan_theme_send_primary else padawan_theme_receive_primary,
                                            shape = RoundedCornerShape(size = 5.dp)
                                        )
                                        .padding(horizontal = 8.dp)
                                ) {
                                    Text(
                                        text = if (txn.isPayment) "Sent" else "Receive",
                                        fontSize = 14.sp,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                    Icon(
                                        painter = if (txn.isPayment) painterResource(id = R.drawable.ic_send_secondary) else painterResource(id = R.drawable.ic_receive_secondary),
                                        contentDescription = if (txn.isPayment) "Send Icon" else "Receive Icon",
                                        modifier = Modifier.scale(scale = 0.75f),
                                        tint = padawan_disabled
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

    val colorTransition = updateTransition(if (currencyState) padawan_theme_onBackground_faded else padawan_theme_onPrimary, label = "")
    val color by colorTransition.animateColor(
        transitionSpec = { tween(durationMillis = 500) },
        label = "Changing Color Animation",
    ) {
        if (it == padawan_theme_onBackground_faded) padawan_theme_onPrimary else padawan_theme_onBackground_faded
    }

    Text(
        text = text.toString().lowercase(),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(all = 8.dp),
        color = color,
    )
}

@Composable
private fun FaucetDialog(walletViewModel: WalletViewModel) {
    AlertDialog(
        backgroundColor = md_theme_dark_lightBackground,
        onDismissRequest = {},
        title = {
            Text(
                text = "Hello there!",
                style = PadawanTypography.headlineMedium,
                color = md_theme_dark_onLightBackground
            )
        },
        text = {
            Text(
                text = "We notice it is your first time opening Padawan wallet. Would you like Padawan to send you some testnet coins to get your started?",
                color = md_theme_dark_onLightBackground
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    walletViewModel.onPositiveDialogClick()
                },
            ) {
                Text(
                    text = "Yes please!",
                    color = md_theme_dark_onLightBackground
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    walletViewModel.onNegativeDialogClick()
                },
            ) {
                Text(
                    text = "Not right now",
                    color = md_theme_dark_onLightBackground
                )
            }
        },
    )
}
