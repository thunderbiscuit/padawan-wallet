/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.components

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
import com.coyotebitcoin.padawanwallet.domain.settings.PadawanColorTheme
import com.coyotebitcoin.padawanwallet.domain.settings.SettingsRepository
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors

@Composable
fun ColorThemePicker() {
    val colors = LocalPadawanColors.current
    val currentTheme = SettingsRepository.getTheme()
    val radioOptions = listOf(PadawanColorTheme.TATOOINE_DESERT, PadawanColorTheme.VADER_DARK)

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(currentTheme) }

    Column {
        radioOptions.forEach { theme: PadawanColorTheme ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (theme == selectedOption),
                        enabled = theme != PadawanColorTheme.VADER_DARK,
                        onClick = {
                            onOptionSelected(theme)
                            SettingsRepository.setTheme(theme)
                        }
                    )
            ) {
                RadioButton(
                    selected = (theme == selectedOption),
                    enabled = theme != PadawanColorTheme.VADER_DARK,
                    onClick = {
                        onOptionSelected(theme)
                        SettingsRepository.setTheme(theme)
                    },
                    colors = RadioButtonDefaults.colors(selectedColor = colors.accent1)
                )
                Text(
                    text = theme.nickname,
                    color = if (theme == PadawanColorTheme.TATOOINE_DESERT) colors.text else colors.textFaded
                )
            }
        }
    }
}
