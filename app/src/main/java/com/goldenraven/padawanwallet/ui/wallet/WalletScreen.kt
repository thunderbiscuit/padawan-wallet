/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Tx
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.ConnectivityStatus
import com.goldenraven.padawanwallet.ui.Screen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
internal fun WalletScreen(
    walletViewModel: WalletViewModel = viewModel(),
    navController: NavHostController
) {
    val balance by walletViewModel.balance.observeAsState()
    val isRefreshing by walletViewModel.isRefreshing.collectAsState()
    val openFaucetDialog by walletViewModel.openFaucetDialog
    val context = LocalContext.current

    if (openFaucetDialog) {
        FaucetDialog(
            walletViewModel = walletViewModel
        )
    }

    if (walletViewModel.isOnline(context = context) && !Wallet.isBlockChainCreated()) {
        Wallet.createBlockchain()
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { walletViewModel.refresh(context = context) },
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxHeight(1f)) {
            val (part1, part2) = createRefs()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(md_theme_dark_background)
                    .verticalScroll(rememberScrollState())
                    .constrainAs(part1) {
                        top.linkTo(parent.top)
                    }
            ) {
                ConnectivityStatus(walletViewModel.isOnline(context = context))
                Spacer(Modifier.padding(24.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(color = md_theme_dark_background2)
                        .height(110.dp)
                        .padding(horizontal = 48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_satoshi),
                        contentDescription = "Satoshi icon",
                        Modifier
                            .align(Alignment.CenterVertically)
                            .size(60.dp)
                    )
                    Text(
                        // DecimalFormat("###,###,###").format(balanceInSatoshis.toLong()).replace(",", "\u2008")
                        balance.toString(),
                        fontFamily = ShareTechMono,
                        fontSize = 40.sp,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = { navController.navigate(Screen.SendScreen.route) },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(80.dp)
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                            .fillMaxWidth(fraction = 0.5f),
                        enabled = walletViewModel.isOnline(context = context)
                    ) {
                        ButtonText(content = "Send")
                    }
                    Button(
                        onClick = { navController.navigate(Screen.ReceiveScreen.route) },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(80.dp)
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                            .fillMaxWidth(),
                        // enabled = walletViewModel.isOnline(context = context)
                    ) {
                        ButtonText(content = "Receive")
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Transaction history",
                        style = PadawanTypography.headlineMedium
                    )
                    Button(
                        onClick = { walletViewModel.refresh(context = context) },
                        colors = ButtonDefaults.buttonColors(md_theme_dark_surface),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.defaultMinSize(
                            minHeight = 10.dp
                        )
                    ) {
                        Text(
                            text = "Sync",
                            style = PadawanTypography.labelSmall
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_refresh),
                            contentDescription = "Sync wallet"
                        )
                    }
                }
                Divider(
                    color = md_theme_dark_surfaceLight,
                    thickness = 1.dp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )
            }
            Spacer(Modifier.padding(24.dp))
            val transactionList by walletViewModel.readAllData.observeAsState(initial = emptyList())
            LazyColumn(
                modifier = Modifier
                    .background(md_theme_dark_background)
                    .padding(bottom = 8.dp)
                    .constrainAs(part2) {
                        top.linkTo(part1.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
            ) {
                items(
                    items = transactionList
                ) { tx ->
                    ExpandableCard(tx)
                }
            }
        }
    }
}

@Composable
internal fun ButtonText(content: String) {
    Text(
        text = content,
        style = PadawanTypography.labelLarge
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExpandableCard(tx: Tx) {
    var expandedState by remember { mutableStateOf(false) }

    Card(
        onClick = { expandedState = !expandedState },
        shape = RoundedCornerShape(8.dp),
        containerColor = md_theme_dark_background2,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(300)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            if (tx.isPayment) {
                Image(
                    painter = painterResource(R.drawable.ic_send),
                    contentDescription = "Send icon",
                    Modifier.size(24.dp)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_receive),
                    contentDescription = "Receive icon"
                )
            }
            Text(
                text = tx.date,
                style = PadawanTypography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            )
        }
        if (expandedState) {
            Divider(
                color = md_theme_dark_surfaceLight,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                expandableCardText("TxId: ", "${tx.txid.take(8)}...${tx.txid.takeLast(8)}")

                if (tx.isPayment) {
                    expandableCardText("Sent: ", "${tx.valueOut} sat")
                } else {
                    expandableCardText("Received: ", "${tx.valueIn} sat")
                }

                expandableCardText("Network fees: ", "${tx.fees} sat")

                if (tx.date == "Pending") {
                    expandableCardTextBottom("Block height: ", "Pending")
                } else {
                    expandableCardTextBottom("Block height: ", "${tx.height}")
                }
            }
        }
    }
}

@Composable
internal fun expandableCardText(title: String, content: String) {
    Text(
        text = buildAnnotatedString {
            append(title)
            withStyle(style = SpanStyle(fontFamily = ShareTechMono)) {
                append(content)
            }
        },
        style = PadawanTypography.labelMedium,
        modifier = Modifier
            .padding(horizontal = 8.dp)
    )
}


@Composable
internal fun expandableCardTextBottom(title: String, content: String) {
    Text(
        text = buildAnnotatedString {
            append(title)
            withStyle(style = SpanStyle(fontFamily = ShareTechMono)) {
                append(content)
            }
        },
        style = PadawanTypography.labelMedium,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(bottom = 16.dp)
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
