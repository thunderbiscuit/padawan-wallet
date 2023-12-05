package com.goldenraven.padawanwallet.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.goldenraven.padawanwallet.ui.theme.padawan_theme_onBackground_faded

@Composable
internal fun FadedVerticalDivider() {
    Divider(
        color = padawan_theme_onBackground_faded,
        modifier = Modifier
            .fillMaxHeight()
            .width(3.dp)
            .padding(vertical = 8.dp)
    )
}
