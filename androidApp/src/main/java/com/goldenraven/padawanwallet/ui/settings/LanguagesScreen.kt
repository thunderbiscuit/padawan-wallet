/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.settings

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.PadawanTypography
import com.goldenraven.padawanwallet.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.theme.padawan_theme_text_faded_secondary
import com.goldenraven.padawanwallet.ui.PadawanAppBar

private const val TAG = "LanguagesScreen"

@Composable
internal fun LanguagesScreen(
    navController: NavHostController
) {
    Column(
        Modifier
            .background(padawan_theme_background_secondary)
            .padding(bottom = 12.dp)
            .fillMaxSize()
    ) {
        PadawanAppBar(navController = navController, title = stringResource(R.string.change_language))
        Text(
            text = stringResource(R.string.select_your_preferred_language),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            style = PadawanTypography.bodyMedium,
            color = padawan_theme_text_faded_secondary
        )

        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        LanguageChoiceRadioButtons()
    }
}

@Composable
fun LanguageChoiceRadioButtons() {
    val radioOptions = listOf(SupportedLanguage.ENGLISH, SupportedLanguage.SPANISH)
    val localeListCompat: LocaleListCompat = AppCompatDelegate.getApplicationLocales()
    Log.i(TAG, "Current locale list compat is: $localeListCompat")

    val currentLanguage: SupportedLanguage = if (localeListCompat[0]?.language == "es") {
        SupportedLanguage.SPANISH
    } else {
        SupportedLanguage.ENGLISH
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
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = (language == selectedOption),
                    onClick = { onOptionSelected(language) }
                )
                Text(
                    // text = language,
                    text = language.name.lowercase().replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

enum class SupportedLanguage {
    ENGLISH,
    SPANISH,
}

fun getSupportedLanguageCode(language: SupportedLanguage): String {
    return when (language) {
        SupportedLanguage.ENGLISH -> "en"
        SupportedLanguage.SPANISH -> "es"
    }
}
