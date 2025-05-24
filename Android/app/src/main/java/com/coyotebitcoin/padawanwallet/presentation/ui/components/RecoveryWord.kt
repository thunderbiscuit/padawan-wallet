/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coyotebitcoin.padawanwallet.presentation.theme.Outfit
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanColorsTatooineDesert
import com.coyotebitcoin.padawanwallet.presentation.theme.standardShadow

@Composable
fun RecoveryWord(
    index: Int,
    word: String,
) {
    Card(
        border = standardBorder,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(PadawanColorsTatooineDesert.accent2),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .standardShadow(20.dp)
            .width(100.dp)
            .height(50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${index + 1}. $word",
                modifier = Modifier.padding(all = 8.dp),
                style = TextStyle(
                    fontFamily = Outfit,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                )
            )
        }
    }
}
