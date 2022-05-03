package com.goldenraven.padawanwallet.ui.wallet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.LiveData
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import org.bitcoindevkit.PartiallySignedBitcoinTransaction

@Composable
internal fun SendScreen() {

    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }

    val recipientAddress: MutableState<String> = remember { mutableStateOf("") }
    val amount: MutableState<String> = remember { mutableStateOf("") }
    val feeRate: MutableState<String> = remember { mutableStateOf("") }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(md_theme_dark_background)
    ) {
        val (screenTitle, transactionInputs, bottomButtons) = createRefs()
        Text(
            text = "Send Bitcoin",
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(screenTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 70.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.constrainAs(transactionInputs) {
                top.linkTo(screenTitle.bottom)
                bottom.linkTo(bottomButtons.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) {
            TransactionRecipientInput(recipientAddress)
            TransactionAmountInput(amount)
            TransactionFeeInput(feeRate)
            Dialog(
                recipientAddress = recipientAddress.value,
                amount = amount.value,
                feeRate = feeRate.value,
                showDialog = showDialog,
                setShowDialog = setShowDialog
            )
        }

        Column(
            Modifier
                .constrainAs(bottomButtons) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 24.dp)
        ) {
            Button(
                onClick = { setShowDialog(true) },
                colors = ButtonDefaults.buttonColors(md_theme_dark_secondary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                    text = "broadcast transaction",
                    fontFamily = jost,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                )
            }
        }
    }
}

@Composable
private fun TransactionRecipientInput(recipientAddress: MutableState<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.9f),
            value = recipientAddress.value,
            onValueChange = { recipientAddress.value = it },
            label = {
                Text(
                    text = "Recipient address",
                )
            },
            singleLine = true,
            textStyle = TextStyle(fontFamily = shareTechMono, color = md_theme_dark_onBackground),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = md_theme_dark_onBackgroundFaded,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Composable
private fun TransactionAmountInput(amount: MutableState<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.9f),
            value = amount.value,
            onValueChange = { value: String ->
                amount.value = value.filter { it.isDigit() }
            },
            singleLine = true,
            textStyle = TextStyle(fontFamily = shareTechMono, color = md_theme_dark_onBackground),
            label = {
                Text(
                    text = "Amount (satoshis)",
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = md_theme_dark_onBackgroundFaded,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Composable
private fun TransactionFeeInput(feeRate: MutableState<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(0.9f),
            value = feeRate.value,
            onValueChange = { newValue: String ->
                feeRate.value = newValue.filter { it.isDigit() }
            },
            singleLine = true,
            textStyle = TextStyle(fontFamily = shareTechMono, color = md_theme_dark_onBackground),
            label = {
                Text(
                    text = "Fee rate",
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = md_theme_dark_onBackgroundFaded,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Composable
fun Dialog(
    recipientAddress: String,
    amount: String,
    feeRate: String,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            containerColor = md_theme_dark_background2,
            onDismissRequest = {},
            title = {
                Text(
                    text = "Confirm transaction",
                )
            },
            text = {
                Text(
                    text = "Send: $amount satoshis \nto: $recipientAddress\nFee rate: ${feeRate.toFloat()}",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        broadcastTransaction(recipientAddress, amount.toULong(), feeRate.toFloat())
                        setShowDialog(false)
                    },
                ) {
                    Text(
                        text = "Confirm",
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        setShowDialog(false)
                    },
                ) {
                    Text(
                        text = "Cancel",
                    )
                }
            },
        )
    }
}

private fun broadcastTransaction(recipientAddress: String, amount: ULong, feeRate: Float = 1F) {
    Log.i("SendScreen", "Attempting to broadcast transaction with inputs: recipient: $recipientAddress, amount: $amount, fee rate: $feeRate")
    try {
        // create, sign, and broadcast
        val psbt: PartiallySignedBitcoinTransaction = Wallet.createTransaction(recipientAddress, amount, feeRate)
        Wallet.sign(psbt)
        val txid: String = Wallet.broadcast(psbt)
        Log.i("SendScreen", "Transaction was broadcast! txid: $txid")
    } catch (e: Throwable) {
        Log.i("SendScreen", "Broadcast error: ${e.message}")
    }
}
