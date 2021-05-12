/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import android.graphics.Color
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.goldenraven.padawanwallet.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun fireSnackbar(view: View, level: SnackbarLevel, message: String, duration: Int = Snackbar.LENGTH_LONG): Unit {

    // set text colour
    val color = when (level) {
        SnackbarLevel.INFO -> Color.argb(255, 235, 219, 178)
        SnackbarLevel.WARNING -> Color.argb(255, 235, 219, 178)
        SnackbarLevel.ERROR -> Color.argb(255, 235, 219, 178)
    }

    // set background
    val background = when (level) {
        SnackbarLevel.INFO -> ResourcesCompat.getDrawable(view.resources, R.drawable.background_toast, null)
        SnackbarLevel.WARNING -> ResourcesCompat.getDrawable(view.resources, R.drawable.background_toast_warning, null)
        SnackbarLevel.ERROR -> ResourcesCompat.getDrawable(view.resources, R.drawable.background_toast_error, null)
    }

    val snackBar = Snackbar.make(view, message, duration)
    snackBar.setTextColor(color)
    snackBar.view.background = background
    snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    snackBar.show()
}

enum class SnackbarLevel {
    INFO,
    WARNING,
    ERROR,
}
