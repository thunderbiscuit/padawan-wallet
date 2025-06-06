/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.domain.utils

import java.util.Locale

val supportedLanguages: List<Locale> = listOf(
    Locale("en"),
    Locale("es"),
    Locale("pt")
)

fun Locale.matchesLanguageOf(other: Locale): Boolean {
    return this.language == other.language
}

sealed class AppLanguage(val language: Locale) {
    object EnglishAsDefaultLanguage : AppLanguage(Locale("en"))
    class OsLevelChoice(osLanguage: Locale): AppLanguage(osLanguage)
    class AppLevelChoice(appLanguage: Locale) : AppLanguage(appLanguage)
}
