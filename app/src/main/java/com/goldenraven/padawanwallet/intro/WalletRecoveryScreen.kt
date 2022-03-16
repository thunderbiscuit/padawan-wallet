/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import android.content.Context
import android.content.Intent
import android.nfc.Tag
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.Wallet
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.utils.buildRecoveryPhrase
import com.goldenraven.padawanwallet.utils.checkWords
import com.goldenraven.padawanwallet.wallet.WalletActivity

private const val TAG = "WalletRecoveryScreen"

@Composable
internal fun WalletRecoveryScreen(onBuildWalletButtonClicked: (WalletCreateType, String?) -> Unit) {
    val context: Context = LocalContext.current

    // the screen is broken into 3 parts
    // the app name, the body, and the button
    ConstraintLayout(modifier = Modifier.fillMaxHeight(1f)) {

        val (appName, body, button) = createRefs()

        val emptyRecoveryPhrase: Map<Int, String> = mapOf(
            1 to "", 2 to "", 3 to "", 4 to "", 5 to "", 6 to "",
            7 to "", 8 to "", 9 to "", 10 to "", 11 to "", 12 to ""
        )
        val (recoveryPhraseWordMap, setRecoveryPhraseWordMap) = remember { mutableStateOf(emptyRecoveryPhrase) }


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
            recoveryPhraseWordMap,
            setRecoveryPhraseWordMap,
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
                        when (checkWords(recoveryPhraseWordMap)) {
                            true -> {
                                val recoveryPhrase: String = buildRecoveryPhrase(recoveryPhraseWordMap)
                                Log.i(TAG, "All words valid!")
                                Log.i(TAG, "Recovery phrase is \"$recoveryPhrase\"")
                                onBuildWalletButtonClicked(WalletCreateType.RECOVER, recoveryPhrase)
                            }
                            false -> {
                                Log.i(TAG, "Not all words are valid")
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

@Composable
fun MyList(
    recoveryPhraseWordMap: Map<Int, String>,
    setRecoveryPhraseWordMap: (Map<Int, String>) -> Unit,
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
        val focusManager = LocalFocusManager.current
        for (i in 1..12) {
            WordField(wordNumber = i, recoveryPhraseWordMap, setRecoveryPhraseWordMap, focusManager)
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

// @Preview(device = Devices.PIXEL_4, showBackground = true)
// @Composable
// internal fun PreviewWalletRecoveryScreen() {
//     PadawanTheme {
//         WalletRecoveryScreen({ })
//     }
// }
