/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

enum class ScreenSizeWidth {
    Small,
    Phone,
    // Tablet,
}

enum class ScreenSizeHeight {
    Small,
    Phone,
    // Tablet,
}

fun getScreenSizeWidth(width: Int): ScreenSizeWidth {
    return when {
        width < 340 -> ScreenSizeWidth.Small
        else -> ScreenSizeWidth.Phone
        // else -> ScreenSize.Tablet
    }
}

fun getScreenSizeHeight(height: Int): ScreenSizeHeight {
    return when {
        height < 700 -> ScreenSizeHeight.Small
        else -> ScreenSizeHeight.Phone
        // else -> ScreenSize.Tablet
    }
}
