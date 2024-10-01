/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.intro

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.theme.PadawanTheme
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.md_theme_dark_onBackgroundFaded
import com.goldenraven.padawanwallet.presentation.theme.md_theme_dark_onLightBackground
import com.goldenraven.padawanwallet.presentation.theme.md_theme_dark_warning
import com.goldenraven.padawanwallet.presentation.theme.standardShadow
import com.goldenraven.padawanwallet.presentation.ui.components.standardBorder
import com.goldenraven.padawanwallet.utils.WalletCreateType
import com.goldenraven.padawanwallet.utils.WordCheckResult
import com.goldenraven.padawanwallet.utils.checkWords
import kotlinx.coroutines.launch

private const val TAG = "WalletRecoveryScreen"

@Composable
internal fun WalletRecoveryScreen(
    onBuildWalletButtonClicked: (WalletCreateType) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            // reuse default SnackbarHost to have default animation and timing handling
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(md_theme_dark_warning)
                        .semantics { testTag = "Intro WalletRecoveryScreen Snackbar" },
                    containerColor = md_theme_dark_warning,

                ) {
                    Text(
                        text = data.visuals.message,
                        style = TextStyle(md_theme_dark_onLightBackground)
                    )
                }
            }
        },
    ) { innerPadding ->
        // the screen is broken into 3 parts
        // the app name, the body, and the button
        ConstraintLayout(
            modifier = Modifier
                .fillMaxHeight(1f)
                .background(Color(0xffffffff))
        ) {

            val (appName, body) = createRefs()

            val emptyRecoveryPhrase: Map<Int, String> = mapOf(
                1 to "", 2 to "", 3 to "", 4 to "", 5 to "", 6 to "",
                7 to "", 8 to "", 9 to "", 10 to "", 11 to "", 12 to ""
            )
            val (recoveryPhraseWordMap, setRecoveryPhraseWordMap) = remember { mutableStateOf(emptyRecoveryPhrase) }


            // the app name
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(innerPadding)
                    .padding(top = 48.dp)
                    .background(Color(0xffffffff))
                    .constrainAs(appName) {
                        top.linkTo(parent.top)
                    }
            ) {
                Text(
                    text = stringResource(R.string.recover_a_wallet),
                    style = PadawanTypography.headlineSmall,
                    color = Color(0xff1f0208),
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.enter_your_12_words),
                    style = PadawanTypography.bodyMedium,
                    color = Color(0xff787878),
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
                )
            }


            // the body
            MyList(
                recoveryPhraseWordMap,
                setRecoveryPhraseWordMap,
                onBuildWalletButtonClicked,
                snackbarHostState,
                modifier = Modifier
                    .constrainAs(body) {
                        top.linkTo(appName.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints

                    },
            )
        }
    }
}

@Composable
fun MyList(
    recoveryPhraseWordMap: Map<Int, String>,
    setRecoveryPhraseWordMap: (Map<Int, String>) -> Unit,
    onBuildWalletButtonClicked: (WalletCreateType) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier
            .fillMaxWidth(1f)
            .background(Color(0xffffffff))
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val scope = rememberCoroutineScope()
        val focusManager = LocalFocusManager.current
        for (i in 1..12) {
            WordField(wordNumber = i, recoveryPhraseWordMap, setRecoveryPhraseWordMap, focusManager)
        }
        Button(
            onClick = {
                when (val wordCheck = checkWords(recoveryPhraseWordMap)) {
                    is WordCheckResult.SUCCESS -> {
                        Log.i(TAG, "All words passed the first check")
                        Log.i(TAG, "Recovery phrase is \"${wordCheck.recoveryPhrase}\"")
                        onBuildWalletButtonClicked(WalletCreateType.RECOVER(wordCheck.recoveryPhrase))
                    }
                    is WordCheckResult.FAILURE -> {
                        Log.i(TAG, "Not all words are valid")
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = wordCheck.errorMessage,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xfff6cf47)),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(top = 12.dp, start = 4.dp, end = 4.dp, bottom = 12.dp)
                .standardShadow(20.dp)
                .height(70.dp)
                .width(240.dp)
                .semantics { testTag = "Intro EnterRecoveryPhrase Button" }
        ) {
            Text(
                stringResource(R.string.recover_wallet),
                textAlign = TextAlign.Center,
            )
        }

    }
}

@Composable
fun WordField(
    wordNumber: Int,
    recoveryWordMap: Map<Int, String>,
    setRecoveryPhraseWordMap: (Map<Int, String>) -> Unit,
    focusManager: FocusManager
) {
    OutlinedTextField(
        value = recoveryWordMap[wordNumber] ?: "elvis is here",
        onValueChange = { newText ->
            val newMap: MutableMap<Int, String> = recoveryWordMap.toMutableMap()
            newMap[wordNumber] = newText

            val updatedMap = newMap.toMap()
            setRecoveryPhraseWordMap(updatedMap)
        },
        label = {
            Text(
                text = "Word $wordNumber",
                color = md_theme_dark_onBackgroundFaded,
            )
        },
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xff2f2f2f),
            unfocusedBorderColor = Color(0xff8a8a8a),
        ),
        modifier = Modifier
            .padding(8.dp),
        keyboardOptions = when (wordNumber) {
            12 -> KeyboardOptions(imeAction = ImeAction.Done)
            else -> KeyboardOptions(imeAction = ImeAction.Next)
        },
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { focusManager.clearFocus() }
        ),
        singleLine = true,
    )
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
internal fun PreviewWalletRecoveryScreen() {
    PadawanTheme {
        WalletRecoveryScreen(
            onBuildWalletButtonClicked = { }
        )
    }
}
