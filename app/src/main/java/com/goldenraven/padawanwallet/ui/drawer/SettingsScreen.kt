package com.goldenraven.padawanwallet.ui.wallet

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.BuildConfig
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Repository
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.ui.DrawerAppBar
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(navController: NavController) {

    val faucetCallDone = Repository.wasOneTimeFaucetCallDone()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = { DrawerAppBar(navController, title = "Settings") },
        bottomBar = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            Modifier.padding(all = 16.dp)
        ) {
            if (faucetCallDone) {
                Button(
                    onClick = {
                        callTatooineFaucet(Wallet.getNewAddress(), coroutineScope, snackbarHostState, context)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.request_testnet_coins))
                }
            } else {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(context.resources.getString(R.string.errorFaucet))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.bg2))
                ) {
                    Text(text = stringResource(id = R.string.request_testnet_coins))
                }
            }
        }
    }
}


private fun callTatooineFaucet(
    address: String,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    context: Context
) {
    val faucetUrl: String = BuildConfig.FAUCET_URL
    val faucetUsername: String = BuildConfig.FAUCET_USERNAME
    val faucetPassword: String = BuildConfig.FAUCET_PASSWORD

    coroutineScope.launch {
        val ktorClient = HttpClient(CIO) {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(
                            username = faucetUsername,
                            password = faucetPassword
                        )
                    }
                }
            }
        }

        Log.i("SettingsFragment", "API call to Tatooine will request coins at $address")
        try {
            val response: HttpResponse = ktorClient.post(faucetUrl) {
                body = TextContent(address, ContentType.Text.Plain)
            }
            Repository.oneTimeFaucetCallDone()
            Log.i("SettingsFragment", "API call to Tatooine was performed. Response is ${response.status}, ${response.readText()}")
        } catch (cause: Throwable) {
            Log.i("SettingsFragment", "Tatooine call failed: $cause")
            coroutineScope.launch { snackbarHostState.showSnackbar(context.resources.getString(R.string.errorFaucet)) }
        }
        ktorClient.close()
    }
}