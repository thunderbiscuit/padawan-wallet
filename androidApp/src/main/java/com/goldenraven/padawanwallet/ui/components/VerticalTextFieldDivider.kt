package com.goldenraven.padawanwallet.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.goldenraven.padawanwallet.theme.padawan_theme_onPrimary

@Composable
internal fun VerticalTextFieldDivider() {
    Divider(
        color = padawan_theme_onPrimary,
        modifier = Modifier
            .fillMaxHeight()
            .width(3.dp)
            .padding(vertical = 14.dp)
    )
}
