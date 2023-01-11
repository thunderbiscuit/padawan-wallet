package com.goldenraven.padawanwallet.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.PadawanAppBar
import com.goldenraven.padawanwallet.utils.*

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

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .standardBackground()
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
