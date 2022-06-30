package com.goldenraven.padawanwallet.ui.wallet

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.data.*
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.utils.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.bitcoindevkit.Transaction
import androidx.compose.material.AlertDialog
import androidx.compose.ui.platform.LocalContext
import org.bitcoindevkit.AddressInfo
import androidx.compose.material3.Card as Card

@Composable
internal fun WalletScreen(
    walletViewModel: WalletViewModel = viewModel(),
    navController: NavHostController
) {
    val balance by walletViewModel.balance.observeAsState()
    val isRefreshing by walletViewModel.isRefreshing.collectAsState()
    val openFaucetDialog by walletViewModel.openFaucetDialog

    if (openFaucetDialog) {
        FaucetDialog(
            walletViewModel = walletViewModel
        )
    }

    if (isOnline(LocalContext.current) && !Wallet.isBlockChainCreated())
        Wallet.createBlockchain()

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
                        fontFamily = shareTechMono,
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
                    ) {
                        Text(
                            text = "Send",
                            fontFamily = sofiaPro,
                            fontSize = 20.sp,
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
                            fontFamily = sofiaPro,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Transaction history",
                        fontFamily = sofiaPro,
                    )
                    Button(
                        onClick = { walletViewModel.refresh() },
                        colors = ButtonDefaults.buttonColors(md_theme_dark_surface),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.defaultMinSize(
                            minHeight = 10.dp
                        )
                    ) {
                        Text(
                            text = "Sync",
                            fontFamily = sofiaPro,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
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
                text = tx.date,
                fontFamily = sofiaPro,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
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
                Text(
                    text = buildAnnotatedString {
                        append("TxId: ")
                        withStyle(style = SpanStyle(fontFamily = shareTechMono)) {
                            append("${tx.txid.take(8)}...${tx.txid.takeLast(8)}")
                        }
                    },
                    fontFamily = sofiaPro,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                if (tx.isPayment) {
                    Text(
                        text = buildAnnotatedString {
                            append("Sent: ")
                            withStyle(style = SpanStyle(fontFamily = shareTechMono)) {
                                append("${tx.valueOut} sat")
                            }
                        },
                        fontFamily = sofiaPro,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                } else {
                    Text(
                        text = buildAnnotatedString {
                            append("Received: ")
                            withStyle(style = SpanStyle(fontFamily = shareTechMono)) {
                                append("${tx.valueIn} sat")
                            }
                        },
                        fontFamily = sofiaPro,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        append("Network fees: ")
                        withStyle(style = SpanStyle(fontFamily = shareTechMono)) {
                            append("${tx.fees} sat")
                        }
                    },
                    fontFamily = sofiaPro,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                if (tx.date == "Pending") {
                    Text(
                        text = buildAnnotatedString {
                            append("Block height: ")
                            withStyle(style = SpanStyle(fontFamily = shareTechMono)) {
                                append("Pending")
                            }
                        },
                        fontFamily = sofiaPro,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 16.dp)
                    )
                } else {
                    Text(
                        text = buildAnnotatedString {
                            append("Block height: ")
                            withStyle(style = SpanStyle(fontFamily = shareTechMono)) {
                                append("${tx.height}")
                            }
                        },
                        fontFamily = sofiaPro,
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

@Composable
private fun FaucetDialog(walletViewModel: WalletViewModel) {
    AlertDialog(
        backgroundColor = md_theme_dark_lightBackground,
        onDismissRequest = {},
        title = {
            Text(
                text = "Hello there!",
                style = MaterialTheme.typography.headlineMedium,
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

internal class WalletViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Tx>>
    private val repository: TxRepository
    var openFaucetDialog: MutableState<Boolean> = mutableStateOf(!didWeOfferFaucet())

    init {
        Log.i("WalletScreen", "The WalletScreen viewmodel is being initialized...")
        val txDao: TxDao = TxDatabase.getDatabase(application).txDao()
        repository = TxRepository(txDao)
        readAllData = repository.readAllData
    }

    fun onPositiveDialogClick() {
        faucetOfferWasMade()
        callTatooineFaucet(getLastUnusedAddress())
        faucetCallDone()
        openFaucetDialog.value = false
    }

    fun onNegativeDialogClick() {
        faucetOfferWasMade()
        openFaucetDialog.value = false
    }

    private fun didWeOfferFaucet(): Boolean {
        val faucetOfferDone = Repository.didWeOfferFaucet()
        Log.i("WalletScreen", "We have already asked if they wanted testnet coins: $faucetOfferDone")
        return faucetOfferDone
    }

    private fun faucetOfferWasMade() {
        Log.i("WalletScreen", "The offer to call the faucet was made")
        Repository.offerFaucetDone()
    }

    private fun faucetCallDone() {
        Repository.faucetCallDone()
    }

    private fun getLastUnusedAddress(): AddressInfo {
        return Wallet.getLastUnusedAddress()
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

    private fun syncTransactionHistory() {
        val txHistory = Wallet.listTransactions()
        Log.i("WalletScreen","Transactions history, number of transactions: ${txHistory.size}")

        for (tx in txHistory) {
            val details = when (tx) {
                is Transaction.Confirmed -> tx.details
                is Transaction.Unconfirmed -> tx.details
            }
            var valueIn = 0
            var valueOut = 0
            val satoshisIn = SatoshisIn(details.received.toInt())
            val satoshisOut = SatoshisOut(details.sent.toInt())
            val isPayment = isPayment(satoshisOut, satoshisIn)
            when (isPayment) {
                true -> {
                    valueOut = netSendWithoutFees(
                        txSatsOut = satoshisOut,
                        txSatsIn = satoshisIn,
                        fees = details.fee?.toInt() ?: 0
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
            val transaction = Tx(
                txid = details.txid,
                date = time,
                valueIn = valueIn,
                valueOut = valueOut,
                fees = details.fee?.toInt() ?: 0,
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

private fun callTatooineFaucet(address: AddressInfo) {
    val faucetUrl: String = BuildConfig.FAUCET_URL
    val faucetUsername: String = BuildConfig.FAUCET_USERNAME
    val faucetPassword: String = BuildConfig.FAUCET_PASSWORD

    // used to be a lifecycleScope.launch because it was in a fragment
    // now simply using a background thread untied to the lifecycle of the composable
    val faucetScope = CoroutineScope(Dispatchers.IO)
    faucetScope.launch {
        val ktorClient = HttpClient(CIO) {
            install(Auth) {
                basic {
                    username = faucetUsername
                    password = faucetPassword
                }
            }
        }

        Log.i("WalletScreen","API call to Tatooine will request coins at $address")
        try {
            val response: HttpResponse = ktorClient.post(faucetUrl) {
                body = TextContent(address.address, ContentType.Text.Plain)
            }
            Repository.faucetCallDone()
            Log.i("WalletScreen","API call to Tatooine was performed. Response is ${response.status}, ${response.readText()}")
        } catch (cause: Throwable) {
            Log.i("WalletScreen","Tatooine call failed: $cause")
        }
        ktorClient.close()
    }
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}