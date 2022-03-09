/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.intro

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.goldenraven.padawanwallet.theme.PadawanTheme

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityShutDown: () -> Unit = {
            this.finish()
        }

        setContent {
            PadawanTheme {
                IntroNavigation(activityShutDown)
            }
        }
    }
}
