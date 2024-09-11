/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
 */

package com.goldenraven.padawanwallet.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LoadingAnimation(
    circleColor: Color = Color(0xfff6cf47),
    circleSize: Dp = 12.dp,
    animationDelay: Int = 800,
    initialAlpha: Float = 0.3f
) {
    val circles = listOf(
        remember { Animatable(initialValue = initialAlpha) },
        remember { Animatable(initialValue = initialAlpha) },
        remember { Animatable(initialValue = initialAlpha) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            // Use coroutine delay to sync animations
            delay(timeMillis = (animationDelay / circles.size).toLong() * index)

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDelay
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    // container for circles
    Row {
        circles.forEachIndexed { index, animatable ->
            // gap between the circles
            if (index != 0) Spacer(modifier = Modifier.width(width = 6.dp))

            Box(
                modifier = Modifier
                    .size(size = circleSize)
                    .clip(shape = CircleShape)
                    .background(circleColor.copy(alpha = animatable.value))
            )
        }
    }
}
