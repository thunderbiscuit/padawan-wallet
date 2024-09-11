/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
 */

package com.goldenraven.padawanwallet.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_onBackground_faded

@Composable
internal fun FadedVerticalDivider() {
    HorizontalDivider(
        color = padawan_theme_onBackground_faded,
        modifier = Modifier
            .fillMaxHeight()
            .width(3.dp)
            .padding(vertical = 8.dp)
    )
}
