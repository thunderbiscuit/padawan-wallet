/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors
import kotlinx.coroutines.delay

@Composable
fun TransactionBroadcastAnimation(
    strokeWidth: Dp = 8.dp,
    durationMillis: Int = 800,
    onAnimationEnd: () -> Unit
) {
    val circleProgress = remember { Animatable(0f) }
    val emojiScale = remember { Animatable(0f) }
    val showText = remember { mutableStateOf(false) }
    val colors = LocalPadawanColors.current

    LaunchedEffect(Unit) {
        circleProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis, easing = LinearOutSlowInEasing)
        )
        emojiScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis / 2, easing = FastOutSlowInEasing)
        )
        showText.value = true
        delay(1500)
        onAnimationEnd()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(120.dp)
        ) {
            // Circle animation
            Canvas(modifier = Modifier.fillMaxSize()) {
                val diameter = size.minDimension
                val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                val size = Size(diameter, diameter)

                drawArc(
                    color = colors.goGreen,
                    startAngle = -90f,
                    sweepAngle = 360 * circleProgress.value,
                    useCenter = false,
                    topLeft = topLeft,
                    size = size,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }

            Text(
                text = "👍",
                fontSize = 48.sp,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = emojiScale.value
                        scaleY = emojiScale.value
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.height(50.dp).padding(top = 16.dp).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column {

                AnimatedVisibility(
                    visible = showText.value,
                    enter = fadeIn(animationSpec = tween(400)) + slideInVertically(initialOffsetY = { it / 2 }),
                ) {
                    Text(
                        text = "Transaction broadcast!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.text,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
