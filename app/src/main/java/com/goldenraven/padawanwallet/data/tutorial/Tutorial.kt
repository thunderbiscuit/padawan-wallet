/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data.tutorial

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.goldenraven.padawanwallet.ui.tutorials.TutorialElement

@Entity(tableName = "tutorial_db")
data class Tutorial(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val type: String,
    val difficulty: String,
    var completion: Int
)