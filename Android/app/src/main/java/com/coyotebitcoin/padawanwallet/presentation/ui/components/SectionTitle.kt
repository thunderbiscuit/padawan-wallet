/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors
import com.coyotebitcoin.padawanwallet.presentation.theme.Outfit

@Composable
fun SectionTitle(title: String, first: Boolean) {
    val colors = LocalPadawanColors.current

    Text(
        modifier = if (first) Modifier.padding(top = 16.dp, start = 4.dp) else Modifier.padding(top = 42.dp, start = 4.dp),
        text = title,
        style = TextStyle(
            fontFamily = Outfit,
            color = colors.text,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    )
}
