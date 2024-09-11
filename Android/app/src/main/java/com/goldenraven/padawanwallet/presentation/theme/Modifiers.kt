/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 0f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}

fun Modifier.standardShadow(cornerRadius: Dp): Modifier = this.then(
    advancedShadow(
        color = Color.Black,
        alpha = 1f,
        cornersRadius = cornerRadius,
        shadowBlurRadius = 0.0001.dp,
        offsetX = 4.dp,
        offsetY = 4.dp
    )
)

val gradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF76dab3),
        Color(0xFF76dab3),
        Color(0xFFf3f4ff)
    ),
    start = Offset(0f, 0f),
    end = Offset(0f, 500f)
)

fun Modifier.gradientBackground(): Modifier = this.then(
    fillMaxSize().background(gradient)
)

fun Modifier.innerScreenPadding(paddingValues: PaddingValues): Modifier = this.then(
    Modifier.padding(paddingValues)
)

fun Modifier.wideTextField(): Modifier = this.then(
    Modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth()
        .border(
            width = 2.dp,
            color = padawan_theme_onPrimary,
            shape = RoundedCornerShape(20.dp)
        )
        .advancedShadow(
            color = Color.Black,
            alpha = 1f,
            cornersRadius = 20.dp,
            shadowBlurRadius = 0.0001.dp,
            offsetX = 4.dp,
            offsetY = 4.dp
        )
)

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}
