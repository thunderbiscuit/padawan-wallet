/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.core.Icon
import com.composables.icons.lucide.ChevronsRight
import com.composables.icons.lucide.Lucide
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors

@Composable
fun ExtraButton(
    label: String,
    onClick: () -> Unit,
) {
    val colors = LocalPadawanColors.current

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = colors.background2),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .size(width = 400.dp, height = 80.dp)
            .padding(start = 24.dp, end = 24.dp, top = 24.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Normal,
            color = colors.textLight
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Lucide.ChevronsRight,
            contentDescription = null,
            tint = colors.accent2
        )
    }
}
