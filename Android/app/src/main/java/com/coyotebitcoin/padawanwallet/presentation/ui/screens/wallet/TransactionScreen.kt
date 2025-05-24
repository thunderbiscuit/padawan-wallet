/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.coyotebitcoin.padawanwallet.domain.bitcoin.TransactionDetails
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.domain.bitcoin.ChainPosition
import com.coyotebitcoin.padawanwallet.domain.bitcoin.TxType
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTypography
import com.coyotebitcoin.padawanwallet.presentation.theme.innerScreenPadding
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanColorsTatooineDesert
import com.coyotebitcoin.padawanwallet.presentation.ui.components.PadawanAppBar
import com.coyotebitcoin.padawanwallet.presentation.utils.ScreenSizeWidth
import com.coyotebitcoin.padawanwallet.presentation.utils.getScreenSizeWidth
import com.coyotebitcoin.padawanwallet.domain.utils.timestampToString

private const val TAG = "TransactionScreen"

@Composable
internal fun TransactionScreen(
    txDetails: TransactionDetails,
    navController: NavHostController,
) {
    val colors = LocalPadawanColors.current
    val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
        ScreenSizeWidth.Small -> PaddingValues(12.dp)
        ScreenSizeWidth.Phone -> PaddingValues(32.dp)
    }

    Scaffold(
        topBar = {
            PadawanAppBar(
                title = stringResource(R.string.transaction_details),
                onClick = { navController.popBackStack() }
            )
        },
        content = { scaffoldPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .innerScreenPadding(padding)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.total_transaction_amount),
                        style = PadawanTypography.titleSmall
                    )
                    val amount = if (txDetails.txType == TxType.OUTBOUND) {
                        txDetails.sent.toSat()
                    } else {
                        txDetails.received.toSat()
                    }
                    Text("$amount sats")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.transaction_type),
                        style = PadawanTypography.titleSmall,
                    )
                    Box {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .background(
                                    color = if (txDetails.txType == TxType.OUTBOUND) colors.accent2 else colors.accent1,
                                    shape = RoundedCornerShape(size = 5.dp)
                                )
                        ) {
                            Text(
                                text = if (txDetails.txType == TxType.OUTBOUND) stringResource(R.string.send) else stringResource(R.string.receive),
                                style = PadawanTypography.bodySmall,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    color = Color(0xff8f8f8f),
                    thickness = 1.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.time),
                        style = PadawanTypography.titleSmall
                    )
                    Text(
                        when (txDetails.chainPosition) {
                            is ChainPosition.Confirmed -> txDetails.chainPosition.timestamp.timestampToString()
                            is ChainPosition.Unconfirmed -> stringResource(R.string.pending, ":")
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.block),
                        style = PadawanTypography.titleSmall,
                    )
                    Text(
                        when (txDetails.chainPosition) {
                            is ChainPosition.Confirmed -> txDetails.chainPosition.height.toString()
                            is ChainPosition.Unconfirmed -> stringResource(R.string.pending, ":")
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.txid),
                        style = PadawanTypography.titleSmall,
                    )
                    val mUriHandler = LocalUriHandler.current
                    val link: String = "https://mempool.space/signet/tx/${txDetails.txid}"
                    Text(
                        text = "${txDetails.txid} (mempool.space)",
                        modifier = Modifier
                            .clickable { mUriHandler.openUri(link) }
                            .width(270.dp),
                        style = PadawanTypography.bodyMedium,
                        color = PadawanColorsTatooineDesert.accent2,
                        textDecoration = TextDecoration.Underline
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val fee = txDetails.fee

                    Text(
                        text = stringResource(R.string.fees_paid),
                        style = PadawanTypography.titleSmall,
                    )
                    Text("${fee.toSat()} sats")
                }
            }
        }
    )
}
