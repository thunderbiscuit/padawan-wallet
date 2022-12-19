/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.ui.DrawerAppBar
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.standardBackground

@Composable
internal fun AboutScreen() {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .verticalScroll(state = scrollState)
            .background(Color(0xffffffff))
    ) {
        Text(text = stringResource(R.string.about_text))
        Spacer(Modifier.height(24.dp))
        Text(text = stringResource(R.string.privacyText))
        Spacer(Modifier.height(24.dp))
        Text(text = stringResource(R.string.privacyLink))
    }
}
