/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.components

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.coyotebitcoin.padawanwallet.domain.settings.PadawanColorTheme
import com.coyotebitcoin.padawanwallet.domain.utils.AppLanguage
import com.coyotebitcoin.padawanwallet.domain.utils.matchesLanguageOf
import com.coyotebitcoin.padawanwallet.domain.utils.supportedLanguages
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme
import java.util.Locale

private const val TAG = "PadawanTag/LanguagePicker"

@Composable
fun SupportedLanguagesPicker() {
    val colors = LocalPadawanColors.current
    val context = LocalContext.current
    val isPerAppLanguageAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    // Choosing a language is tricky, and works differently on different Android versions.
    // On Android 13 and above, we can use the LocaleManager to get an application-specific language setting.
    // On older versions, we fall back to the system default locale.
    // If the default system locale is not supported, we default to English.
    var currentLocale: AppLanguage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val localeManager: LocaleManager = context.getSystemService(LocaleManager::class.java)
        val currentApplicationLocales: LocaleList = localeManager.applicationLocales
        Log.i(TAG, "Current application locales are: $currentApplicationLocales")
        if (currentApplicationLocales.isEmpty) {
            Log.i(TAG, "No application locales set, using default system locale.")
            val systemLevelLocale: Locale = Locale.getDefault()
            Log.i(TAG, "Current system locale is: $systemLevelLocale")
            val match = supportedLanguages.find { it.matchesLanguageOf(systemLevelLocale) }
            if (match != null) {
                Log.i(TAG, "System locale '$match' is supported. We are using it as the app language.")
                AppLanguage.OsLevelChoice(systemLevelLocale)
            } else {
                Log.i(TAG, "System locale not supported, defaulting to English.")
                AppLanguage.EnglishAsDefaultLanguage
            }
        } else if (currentApplicationLocales[0] in supportedLanguages) {
            AppLanguage.AppLevelChoice(currentApplicationLocales[0])
        } else {
            Log.i(TAG, "Current application locale not supported, defaulting to English.")
            AppLanguage.EnglishAsDefaultLanguage
        }
    } else {
        // For Android versions below 13, we use the system default locale if we can, otherwise default to English.
        val systemLevelLocale: Locale = Locale.getDefault()
        Log.i(TAG, "Current system locale is: $systemLevelLocale")
        if (systemLevelLocale in supportedLanguages) {
            AppLanguage.OsLevelChoice(systemLevelLocale)
        } else {
            Log.i(TAG, "System locale not supported, defaulting to English.")
            AppLanguage.EnglishAsDefaultLanguage
        }
    }

    Log.i(TAG, "Current locale for the app is: ${currentLocale.language}")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(currentLocale.language) }

    Column {
        if (isPerAppLanguageAvailable) {
            supportedLanguages.forEach { language ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (language.language == selectedOption.language),
                            onClick = {
                                onOptionSelected(language)
                                val appLocale = Locale.forLanguageTag(language.language)
                                // This will only ever be called on Android 13+ devices.
                                val localeManager: LocaleManager = context.getSystemService(LocaleManager::class.java)
                                localeManager.applicationLocales = LocaleList(appLocale)
                                currentLocale = AppLanguage.AppLevelChoice(appLocale)
                            }
                        )
                ) {
                    RadioButton(
                        selected = (language.language == selectedOption.language),
                        onClick = {
                            onOptionSelected(language)
                            val appLocale = Locale.forLanguageTag(language.language)
                            // This will only ever be called on Android 13+ devices.
                            val localeManager: LocaleManager = context.getSystemService(LocaleManager::class.java)
                            localeManager.applicationLocales = LocaleList(appLocale)
                            currentLocale = AppLanguage.AppLevelChoice(appLocale)
                        },
                        colors = RadioButtonDefaults.colors(selectedColor = colors.accent1)
                    )
                    Text(
                        text = language.displayLanguage.lowercase().replaceFirstChar { it.uppercase() },
                    )
                }
            }
        } else {
            Text(
                text = "App-level language selection is only available on Android 13+ devices.",
                style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 14.sp),
            )
            supportedLanguages.forEach { language ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (language.language == selectedOption.language),
                            enabled = false,
                            onClick = {}
                        )
                ) {
                    RadioButton(
                        selected = (language.language == selectedOption.language),
                        enabled = false,
                        onClick = {},
                        colors = RadioButtonDefaults.colors(selectedColor = colors.accent1)
                    )
                    Text(
                        text = language.displayLanguage.lowercase().replaceFirstChar { it.uppercase() },
                    )
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
fun PreviewSupportedLanguagesPicker() {
    PadawanTheme(PadawanColorTheme.TATOOINE_DESERT) {
        SupportedLanguagesPicker()
    }
}
