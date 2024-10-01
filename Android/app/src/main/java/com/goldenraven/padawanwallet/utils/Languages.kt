/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

enum class SupportedLanguage {
    ENGLISH,
    SPANISH,
    PORTUGUESE,
}

fun getSupportedLanguageCode(language: SupportedLanguage): String {
    return when (language) {
        SupportedLanguage.ENGLISH -> "en"
        SupportedLanguage.SPANISH -> "es"
        SupportedLanguage.PORTUGUESE -> "pt"
    }
}

fun setLanguage() {
    val localeListCompat: LocaleListCompat = AppCompatDelegate.getApplicationLocales()

    if (localeListCompat.isEmpty) {
        val defaultSystemLocale = Locale.getDefault().language

        if (defaultSystemLocale.contains("es")) {
            val languageCode: String = getSupportedLanguageCode(SupportedLanguage.SPANISH)
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(appLocale)
        } else if (defaultSystemLocale.contains("pt")) {
            val languageCode: String = getSupportedLanguageCode(SupportedLanguage.PORTUGUESE)
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(appLocale)
        } else {
            val languageCode: String = getSupportedLanguageCode(SupportedLanguage.ENGLISH)
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }
}
