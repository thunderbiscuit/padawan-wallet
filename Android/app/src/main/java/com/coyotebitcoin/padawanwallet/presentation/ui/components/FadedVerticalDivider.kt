/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanColorsTatooineDesert

@Composable
internal fun FadedVerticalDivider() {
    HorizontalDivider(
        color = PadawanColorsTatooineDesert.text,
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .padding(vertical = 8.dp)
    )
}
