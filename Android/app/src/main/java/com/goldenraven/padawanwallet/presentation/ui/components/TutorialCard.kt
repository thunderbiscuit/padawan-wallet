/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
 */

package com.goldenraven.padawanwallet.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.core.Icon
import com.composables.icons.lucide.Circle
import com.composables.icons.lucide.Lucide
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.theme.Outfit
import com.goldenraven.padawanwallet.presentation.theme.PadawanTheme
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_background
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_onBackground_secondary
import com.goldenraven.padawanwallet.presentation.theme.standardShadow

@Composable
fun TutorialCard(
    title: String,
    done: Boolean,
    onClick: () -> Unit,
) {
    Card(
        border = standardBorder,
        shape = RoundedCornerShape(20.dp),
        colors = if (done) CardDefaults.cardColors(padawan_theme_background) else CardDefaults.cardColors(padawan_theme_onBackground_secondary),
        modifier = Modifier
            .padding(vertical = 4.dp)
            .standardShadow(20.dp)
            .height(70.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(all = 16.dp),
                style = TextStyle(
                    fontFamily = Outfit,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                ),
            )
            if (done) {
                Image(
                    painter = painterResource(id = R.drawable.lucide_star),
                    contentDescription = "Done",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 16.dp)
                )
            } else {
                Image(
                    imageVector = Lucide.Circle,
                    contentDescription = "Not done",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 16.dp)
                )
            }
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
internal fun PreviewNotCompletedTutorialCard() {
    PadawanTheme {
        TutorialCard(
            title = "Tutorial Card",
            done = false,
            onClick = { }
        )
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
internal fun PreviewCompletedTutorialCard() {
    PadawanTheme {
        TutorialCard(
            title = "Tutorial Card",
            done = true,
            onClick = { }
        )
    }
}
