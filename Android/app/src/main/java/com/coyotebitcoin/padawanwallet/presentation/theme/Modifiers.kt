/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

// The Neubrutalism shadow we use everywhere in Padawan. It uses a RoundedCornerShape(20.dp) that's hardcoded in the
// modifier, and therefore must be applied on components that also use RoundedCornerShape(20.dp).
fun Modifier.neuBrutalismShadow(): Modifier {
    val blackBrush: Brush = SolidColor(Color.Black)
    val shadow = Shadow(
        radius = 0.dp,
        brush = blackBrush,
        spread = 0.dp,
        offset = DpOffset(4.dp, 4.dp),
        alpha = 1f,
    )
    return this.dropShadow(shape = RoundedCornerShape(20.dp), shadow = shadow)
}

fun Modifier.innerScreenPadding(paddingValues: PaddingValues): Modifier = this.then(
    Modifier.padding(paddingValues)
)

fun Modifier.wideTextField(): Modifier = this.then(
    Modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth()
        .border(
            width = 2.dp,
            color = Color.Black,
            shape = RoundedCornerShape(20.dp)
        )
        .neuBrutalismShadow()
)

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}
