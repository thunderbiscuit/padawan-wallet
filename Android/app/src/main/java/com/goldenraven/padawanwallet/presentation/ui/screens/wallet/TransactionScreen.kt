/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.wallet

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
// import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.composables.core.Icon
import com.composables.icons.lucide.ArrowDownToLine
import com.composables.icons.lucide.ArrowUpFromLine
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Send
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.domain.bitcoin.ChainPosition
import com.goldenraven.padawanwallet.domain.bitcoin.TransactionDetails
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.gradientBackground
import com.goldenraven.padawanwallet.presentation.theme.innerScreenPadding
import com.goldenraven.padawanwallet.presentation.theme.padawan_disabled
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_receive_primary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_send_primary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_button_primary
import com.goldenraven.padawanwallet.presentation.ui.components.PadawanAppBar
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.TxType
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import com.goldenraven.padawanwallet.utils.timestampToString

private const val TAG = "TransactionScreen"

@Composable
internal fun TransactionScreen(
    txDetails: TransactionDetails,
    navController: NavHostController,
) {
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
                    .gradientBackground()
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
                    Text("${txDetails.received.toSat() + txDetails.sent.toSat()} sats")
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
                                    color = if (txDetails.txType == TxType.PAYMENT) padawan_theme_send_primary else padawan_theme_receive_primary,
                                    shape = RoundedCornerShape(size = 5.dp)
                                )
                        ) {
                            Text(
                                text = if (txDetails.txType == TxType.PAYMENT) stringResource(R.string.send) else stringResource(R.string.receive),
                                style = PadawanTypography.bodySmall,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                            )
                            Icon(
                                imageVector = if (txDetails.txType == TxType.PAYMENT) Lucide.ArrowUpFromLine else Lucide.ArrowDownToLine,
                                tint = padawan_disabled,
                                contentDescription = if (txDetails.txType == TxType.PAYMENT) stringResource(R.string.send_icon) else stringResource(R.string.receive_icon),
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .scale(scale = 0.75f)
                                    .padding(end = 8.dp),
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
                        color = padawan_theme_button_primary,
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
