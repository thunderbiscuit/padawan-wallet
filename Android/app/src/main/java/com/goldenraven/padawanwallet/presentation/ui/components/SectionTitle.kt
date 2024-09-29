/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.goldenraven.padawanwallet.presentation.theme.Outfit
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_text_headline

@Composable
fun SectionTitle(title: String, first: Boolean) {
    Text(
        modifier = if (first) Modifier.padding(top = 16.dp, start = 4.dp) else Modifier.padding(top = 42.dp, start = 4.dp),
        text = title,
        style = TextStyle(
            fontFamily = Outfit,
            color = padawan_theme_text_headline,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    )
}
