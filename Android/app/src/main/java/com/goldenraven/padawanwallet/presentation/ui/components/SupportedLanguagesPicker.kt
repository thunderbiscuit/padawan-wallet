/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
 */

package com.goldenraven.padawanwallet.presentation.ui.components

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import com.goldenraven.padawanwallet.presentation.theme.PadawanTheme
import com.goldenraven.padawanwallet.utils.SupportedLanguage
import com.goldenraven.padawanwallet.utils.getSupportedLanguageCode

private const val TAG = "SupportedLanguagesPicker"

@Composable
fun SupportedLanguagesPicker() {
    val radioOptions = listOf(SupportedLanguage.ENGLISH, SupportedLanguage.SPANISH, SupportedLanguage.PORTUGUESE)
    val localeListCompat: LocaleListCompat = AppCompatDelegate.getApplicationLocales()
    Log.i(TAG, "Current locale list compat is: $localeListCompat")

    val currentLanguage: SupportedLanguage = when (localeListCompat[0]?.language) {
        "es" -> SupportedLanguage.SPANISH
        "pt" -> SupportedLanguage.PORTUGUESE
        else -> SupportedLanguage.ENGLISH
    }

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(currentLanguage) }

    Column {
        radioOptions.forEach { language: SupportedLanguage ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (language == selectedOption),
                        onClick = {
                            onOptionSelected(language)
                            val languageCode: String = getSupportedLanguageCode(language)
                            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
                            AppCompatDelegate.setApplicationLocales(appLocale)
                        }
                    )
            ) {
                RadioButton(
                    selected = (language == selectedOption),
                    onClick = {
                        onOptionSelected(language)
                        val languageCode: String = getSupportedLanguageCode(language)
                        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
                        AppCompatDelegate.setApplicationLocales(appLocale)
                    }
                )
                Text(
                    text = language.name.lowercase().replaceFirstChar { it.uppercase() },
                )
            }
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
fun PreviewSupportedLanguagesPicker() {
    PadawanTheme {
        SupportedLanguagesPicker()
    }
}
