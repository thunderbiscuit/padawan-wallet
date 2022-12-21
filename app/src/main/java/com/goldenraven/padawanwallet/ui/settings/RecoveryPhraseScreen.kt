/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.goldenraven.padawanwallet.data.WalletRepository
import com.goldenraven.padawanwallet.theme.PadawanTypography
import com.goldenraven.padawanwallet.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.theme.padawan_theme_text_headline
import com.goldenraven.padawanwallet.theme.standardShadow
import com.goldenraven.padawanwallet.ui.standardBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecoveryPhraseScreen() {
    val seedPhrase: String = WalletRepository.getMnemonic()
    val wordList: List<String> = seedPhrase.split(" ")

    Column (
        modifier = Modifier
            .background(padawan_theme_background_secondary)
            .fillMaxSize()
    ){
        Text(
            text = "Your wallet recovery phrase",
            style = PadawanTypography.headlineSmall,
            color = padawan_theme_text_headline,
            modifier = Modifier
                .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 32.dp)
        )
        wordList.forEachIndexed { index, item ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
            ) {
                Text(text = "${index + 1}: ")
                Card(
                    border = standardBorder,
                ) {
                    Text(
                        text = item,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }
}
