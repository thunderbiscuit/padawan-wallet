/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

public fun fireSnackbar(view: View, level: SnackbarLevel, message: String): Unit {
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)

    // set text colour
    when (level) {
        SnackbarLevel.INFO -> snackBar.setTextColor(Color.argb(255, 235, 219, 178))
        SnackbarLevel.WARNING -> snackBar.setTextColor(Color.argb(255, 235, 219, 178))
        SnackbarLevel.ERROR -> snackBar.setTextColor(Color.argb(255, 235, 219, 178))
    }

    // set background colour
    when (level) {
        SnackbarLevel.INFO -> snackBar.view.background = view.resources.getDrawable(R.drawable.background_toast, null)
        SnackbarLevel.WARNING -> snackBar.view.background = view.resources.getDrawable(R.drawable.background_toast_warning, null)
        SnackbarLevel.ERROR -> snackBar.view.background = view.resources.getDrawable(R.drawable.background_toast_error, null)
    }

    snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
    snackBar.show()
}


enum class SnackbarLevel {
    INFO,
    WARNING,
    ERROR,
}
