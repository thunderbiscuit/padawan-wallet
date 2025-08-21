/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.domain.bitcoin.WalletRepository
import com.coyotebitcoin.padawanwallet.presentation.ui.components.PadawanAppBar
import com.coyotebitcoin.padawanwallet.presentation.ui.components.RecoveryWord

@Composable
internal fun RecoveryPhraseScreen(
    onBack: () -> Unit,
) {
    val seedPhrase: String = WalletRepository.getMnemonic()
    val wordList: List<String> = seedPhrase.split(" ")

    Scaffold(
        topBar = {
            PadawanAppBar(
                title = stringResource(R.string.your_recovery_phrase),
                onClick = { onBack() }
            )
        }
    ) { scaffoldPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            itemsIndexed(wordList) { index, item ->
                RecoveryWord(index, item)
            }
        }
    }
}
