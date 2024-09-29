/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.utils

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
