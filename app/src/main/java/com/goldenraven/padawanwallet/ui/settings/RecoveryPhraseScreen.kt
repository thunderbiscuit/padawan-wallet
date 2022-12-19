/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.goldenraven.padawanwallet.data.WalletRepository

@Composable
internal fun RecoveryPhraseScreen() {

    val seedPhrase: String = WalletRepository.getMnemonic()
    val wordList: List<String> = seedPhrase.split(" ")

    Column (
        modifier = Modifier
            .padding(all = 32.dp)
            .background(Color(0xffffffff))
            .fillMaxSize()
    ){
        wordList.forEachIndexed { index, item ->
            Text(
                text = "${index + 1}: $item",
                modifier = Modifier.weight(weight = 1F)
            )
        }
    }
}
