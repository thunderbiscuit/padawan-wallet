/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.utils.AutoCompleteMnemonic
import com.goldenraven.padawanwallet.utils.WordCheckResult
import com.goldenraven.padawanwallet.utils.checkWords
import com.goldenraven.padawanwallet.utils.validWords
import kotlinx.coroutines.launch

private const val TAG = "WalletRecoveryScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WalletRecoveryScreen(onBuildWalletButtonClicked: (WalletCreateType) -> Unit) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        // snackbarHost = { SnackbarHost(snackbarHostState) }
        snackbarHost = {
            // reuse default SnackbarHost to have default animation and timing handling
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(md_theme_dark_warning),
                    containerColor = md_theme_dark_warning,

                    ) {
                    Text(
                        text = data.visuals.message,
                        style = TextStyle(md_theme_dark_onLightBackground)
                    )
                }
            }
        },
    ) {

        // the screen is broken into 3 parts
        // the app name, the body, and the button
        ConstraintLayout(modifier = Modifier.fillMaxHeight(1f)) {

            val (appName, body, button) = createRefs()

            val emptyRecoveryPhrase: Map<Int, String> = mapOf(
                1 to "", 2 to "", 3 to "", 4 to "", 5 to "", 6 to "",
                7 to "", 8 to "", 9 to "", 10 to "", 11 to "", 12 to ""
            )
            val (recoveryPhraseWordMap) = remember {
                mutableStateOf(
                    emptyRecoveryPhrase
                )
            }


            // the app name
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .background(MaterialTheme.colorScheme.background)
                    .constrainAs(appName) {
                        top.linkTo(parent.top)
                    }
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.padawan),
                        color = md_theme_dark_primary,
                        fontSize = 70.sp,
                        fontFamily = shareTechMono,
                        modifier = Modifier
                            .padding(top = 70.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        stringResource(R.string.elevator_pitch),
                        color = md_theme_dark_onBackground,
                        fontSize = 14.sp,
                        fontFamily = rubik,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }


            // the body
            MyList(
                modifier = Modifier
                    .constrainAs(body) {
                        top.linkTo(appName.bottom)
                        bottom.linkTo(button.top)
                        height = Dimension.fillToConstraints
                    })


            // the button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .constrainAs(button) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Column {
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
                        colors = ButtonDefaults.buttonColors(md_theme_dark_primary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .size(width = 300.dp, height = 100.dp)
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            stringResource(R.string.recover_wallet),
                            fontSize = 20.sp,
                            fontFamily = rubik,
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyList(
    modifier: Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier
            .fillMaxWidth(1f)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        for (i in 1..12) {
            AutoCompleteMnemonic(validWords)
        }
    }
}

// @OptIn(ExperimentalComposeUiApi::class)
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
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = md_theme_dark_onBackgroundFaded,
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


@Composable
fun SearchText(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onFocusChanged: (FocusState) -> (Unit) = {},
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { query ->
            onValueChanged(query)
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChanged(it) },
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.headlineSmall,
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { onClearClick() }) {
                Icon(Icons.Filled.Clear, "Clear")
            }
        },
        keyboardActions = KeyboardActions({ onDoneActionClick() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )
}

// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewWalletRecoveryScreen() {
//     PadawanTheme {
//         WalletRecoveryScreen({ })
//     }
// }