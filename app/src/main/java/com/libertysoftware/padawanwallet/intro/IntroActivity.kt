/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.libertysoftware.padawanwallet.intro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.libertysoftware.padawanwallet.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }
}
