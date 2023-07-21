/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BackgroundShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = Path().apply {
                reset()
                moveTo(x = 0f, y = size.height / 5)
                quadraticBezierTo(x1 = size.width / 2, y1 = size.height / 7, x2 = size.width, y2 = size.height / 5)
                addRect(rect = Rect(
                    top = size.height / 5,
                    bottom = size.height,
                    left = 0f,
                    right = size.width,
                    )
                )
                close()
            }
        )
    }
}
