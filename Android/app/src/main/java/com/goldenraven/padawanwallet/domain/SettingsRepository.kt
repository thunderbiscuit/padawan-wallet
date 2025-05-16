/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain

import android.content.SharedPreferences
import androidx.core.content.edit

object SettingsRepository {
    private lateinit var sharedPreferences: SharedPreferences
    fun setSharedPreferences(sharedPref: SharedPreferences) {
        sharedPreferences = sharedPref
    }

    fun getTheme(): PadawanColorTheme {
        val theme: Int = sharedPreferences.getInt("theme", PadawanColorTheme.TATOOINE_DESERT.id)
        return PadawanColorTheme.fromId(theme)
    }

    fun setTheme(theme: PadawanColorTheme) {
        sharedPreferences.edit {
            putInt("theme", theme.id)
        }
    }
}

enum class PadawanColorTheme(val id: Int, val nickname: String) {
    TATOOINE_DESERT(0, "Tatooine Desert"),
    VADER_DARK(1, "Vader Dark (Coming Soon!)");

    companion object {
        fun fromId(id: Int): PadawanColorTheme {
            return entries.find { it.id == id } ?: TATOOINE_DESERT
        }
    }
}
