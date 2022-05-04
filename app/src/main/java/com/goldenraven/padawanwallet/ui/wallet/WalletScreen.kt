package com.goldenraven.padawanwallet.ui.wallet

import android.app.Application
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import com.goldenraven.padawanwallet.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.data.*
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.utils.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.bitcoindevkit.Transaction
import androidx.compose.material3.Card as Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WalletScreen(
    walletViewModel: WalletViewModel = viewModel(),
    navController: NavHostController
) {
    val balance by walletViewModel.balance.observeAsState()
    // walletViewModel.updateBalance()

    // val refreshViewModel: RefreshViewModel = viewModel()
    val isRefreshing by walletViewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { walletViewModel.refresh() },
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
                Spacer(Modifier.padding(24.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(color = md_theme_dark_background2)
                        .height(110.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sat),
                        contentDescription = "Bitcoin testnet logo",
                        Modifier
                            .align(Alignment.CenterVertically)
                            .size(60.dp)
                    )
                    Text(
                        balance.toString(),
                        fontFamily = shareTechMono,
                        fontSize = 32.sp,
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
                    ) {
                        Text(
                            text = "Send",
                            fontFamily = jost,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp,
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                        )
                    }
                    Button(
                        onClick = { navController.navigate(Screen.ReceiveScreen.route) },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .height(80.dp)
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            text = "Receive",
                            fontFamily = jost,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Text(
                    text = "Transaction history",
                    fontFamily = jost,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCard(tx: Tx) {
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
                text = "${tx.txid.take(8)}...${tx.txid.takeLast(8)}",
                fontFamily = shareTechMono,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )
        }
        if (expandedState) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Time: ${tx.date}",
                    fontFamily = jost,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = "Fees: ${tx.fees} sats",
                    fontFamily = jost,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                if (tx.isPayment) {
                    Text(
                        text = "Sent: ${tx.valueOut} sats",
                        fontFamily = jost,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 16.dp)
                    )
                } else {
                    Text(
                        text = "Received: ${tx.valueIn} sats",
                        fontFamily = jost,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}

internal class WalletViewModel(application: Application) : AndroidViewModel(application) {

    val app: Application = application
    val readAllData: LiveData<List<Tx>>
    private val repository: TxRepository

    init {
        val txDao: TxDao = TxDatabase.getDatabase(application).txDao()
        repository = TxRepository(txDao)
        readAllData = repository.readAllData
    }

    private var _balance: MutableLiveData<ULong> = MutableLiveData(0u)
    val balance: LiveData<ULong>
        get() = _balance

    private fun updateBalance() {
        Wallet.sync()
        _balance.value = Wallet.getBalance()
    }

    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    fun refresh() {
        // This doesn't handle multiple 'refreshing' tasks, don't use this
        viewModelScope.launch {
            // A fake 2 second 'refresh'
            _isRefreshing.emit(true)
            updateBalance()
            syncTransactionHistory()
            delay(300)
            _isRefreshing.emit(false)
        }
    }

    fun syncTransactionHistory() {
        val txHistory = Wallet.listTransactions()
        Log.i("WalletScreen","Transactions history, number of transactions: ${txHistory.size}")

        for (tx in txHistory) {
            val details = when (tx) {
                is Transaction.Confirmed -> tx.details
                is Transaction.Unconfirmed -> tx.details
            }
            var valueIn: Int = 0
            var valueOut: Int = 0
            val satoshisIn = SatoshisIn(details.received.toInt())
            val satoshisOut = SatoshisOut(details.sent.toInt())
            val isPayment = isPayment(satoshisOut, satoshisIn)
            when (isPayment) {
                true -> {
                    valueOut = netSendWithoutFees(
                        txSatsOut = satoshisOut,
                        txSatsIn = satoshisIn,
                        fees = details.fees?.toInt() ?: 0
                    )
                }
                false -> {
                    valueIn = details.received.toInt()
                }
            }
            val time: String = when (tx) {
                is Transaction.Confirmed -> tx.confirmation.timestamp.timestampToString()
                else -> "Pending"
            }
            val height: UInt = when (tx) {
                is Transaction.Confirmed -> tx.confirmation.height
                else -> 100_000_000u
            }
            val transaction: Tx = Tx(
                txid = details.txid,
                date = time,
                valueIn = valueIn,
                valueOut = valueOut,
                fees = details.fees?.toInt() ?: 0,
                isPayment = isPayment,
                height = height.toInt()
            )
            addTx(transaction)
        }
    }

    private fun addTx(tx: Tx) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("WalletScreen", "Adding transaction to DB: $tx")
            repository.addTx(tx)
        }
    }
}

