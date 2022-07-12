/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.data.Repository
import com.goldenraven.padawanwallet.ui.DrawerAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecoveryPhraseScreen(navController: NavController) {

    val seedPhrase: String = Repository.getMnemonic()
    val wordList: List<String> = seedPhrase.split(" ")

    Scaffold(
        topBar = { DrawerAppBar(navController, title = "Wallet Recovery Phrase") },
    ) {
        Column (
            modifier = Modifier.padding(all = 32.dp)
        ){
            wordList.forEachIndexed { index, item ->
                Text(
                    text = "${index + 1}: $item",
                    modifier = Modifier.weight(weight = 1F)
                )
            }
        }
    }
}
