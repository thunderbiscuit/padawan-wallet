/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.PadawanTypography
import com.goldenraven.padawanwallet.theme.padawan_disabled
import com.goldenraven.padawanwallet.theme.padawan_theme_receive_primary
import com.goldenraven.padawanwallet.theme.padawan_theme_send_primary
import com.goldenraven.padawanwallet.theme.standardBackground
import com.goldenraven.padawanwallet.ui.PadawanAppBar
import com.goldenraven.padawanwallet.utils.SatoshisIn
import com.goldenraven.padawanwallet.utils.SatoshisOut
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import com.goldenraven.padawanwallet.utils.isPayment
import com.goldenraven.padawanwallet.utils.parseTxAmounts
import com.goldenraven.padawanwallet.utils.timestampToString

private const val TAG = "TransactionScreen"

@Composable
internal fun TransactionScreen(
    navController: NavHostController,
    txid: String?,
) {
    if (txid == null) {
        navController.popBackStack()
    }
    val transaction = Wallet.getTransaction(txid = txid!!)
    if (transaction == null) {
        navController.popBackStack()
    }

    val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
        ScreenSizeWidth.Small -> 12.dp
        ScreenSizeWidth.Phone -> 32.dp
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .standardBackground(padding)
    ) {
        val (screenTitle, QRCode) = createRefs()

        Row(
            Modifier
                .constrainAs(screenTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            PadawanAppBar(navController = navController, title = "Transaction details")
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .constrainAs(QRCode) {
                    top.linkTo(screenTitle.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val fee = transaction!!.fee ?: 0uL
                val txInfo = parseTxAmounts(transaction)
                Text(
                    text = "Total transaction amount:",
                    style = PadawanTypography.titleSmall
                )
                Text("${txInfo.valueIn + txInfo.valueOut + fee } sats")
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val isPayment: Boolean = isPayment(SatoshisOut(transaction!!.sent.toInt()), SatoshisIn(transaction.received.toInt()))
                Text(
                    text = "Transaction type:",
                    style = PadawanTypography.titleSmall,
                )
                Box {
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .background(
                                color = if (isPayment) padawan_theme_send_primary else padawan_theme_receive_primary,
                                shape = RoundedCornerShape(size = 5.dp)
                            )
                    ) {
                        Text(
                            text = if (isPayment) "Sent" else "Receive",
                            style = PadawanTypography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                        )
                        Icon(
                            painter = if (isPayment) painterResource(id = R.drawable.ic_send_secondary) else painterResource(id = R.drawable.ic_receive_secondary),
                            tint = padawan_disabled,
                            contentDescription = if (isPayment) "Send Icon" else "Receive Icon",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .scale(scale = 0.75f)
                                .padding(end = 8.dp),
                        )
                    }
                }
            }
            Divider(
                modifier = Modifier.fillMaxWidth().padding(40.dp),
                color = Color(0xff8f8f8f),
                thickness = 1.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Time:",
                    style = PadawanTypography.titleSmall
                )
                Text(transaction!!.confirmationTime?.timestamp?.timestampToString() ?: "Pending")
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Block:",
                    style = PadawanTypography.titleSmall,
                )
                Text("${transaction!!.confirmationTime?.height ?: "Pending"}")
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Txid:",
                    style = PadawanTypography.titleSmall,
                )
                Text(
                    modifier = Modifier.width(170.dp),
                    text = transaction!!.txid
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val fee = transaction!!.fee ?: 0uL

                Text(
                    text = "Fees paid:",
                    style = PadawanTypography.titleSmall,
                )
                Text("$fee sats")
            }
        }
    }
}
