/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.chapters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.Outfit
import com.goldenraven.padawanwallet.theme.PadawanTypography
import com.goldenraven.padawanwallet.theme.innerScreenPadding
import com.goldenraven.padawanwallet.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.theme.padawan_theme_text_headline
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.components.TutorialCard
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth

// TODO: Remember "next up" chapter

private const val TAG = "ChaptersRootScreen"

@Composable
internal fun ChaptersRootScreen(chaptersViewModel: ChaptersViewModel, navController: NavController) {
    val completedChapters = chaptersViewModel.getCompletedChapters()
    val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
        ScreenSizeWidth.Small -> 12.dp
        ScreenSizeWidth.Phone -> 18.dp
    }

    Column(
        Modifier.background(padawan_theme_background_secondary)
    ) {
        Text(
            text = stringResource(R.string.padawan_journey),
            style = PadawanTypography.headlineSmall,
            color = Color(0xff1f0208),
            modifier = Modifier.padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 8.dp)
        )
        Text(
            text = stringResource(R.string.continue_on_your_journey),
            color = Color(0xff787878),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 4.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .innerScreenPadding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            SectionTitle("Getting Started", true)
            SectionDivider()
            TutorialCard(
                title = "1. What is the Bitcoin testnet?",
                done = completedChapters[1] ?: false,
                onClick = { navController.navigate(route = "${Screen.ChapterScreen.route}/1") }
            )
            TutorialCard(
                title = "2. Receiving bitcoin",
                done = completedChapters[2] ?: false,
                onClick = { navController.navigate(route = "${Screen.ChapterScreen.route}/2") }
            )
            TutorialCard(
                title = "3. Sending bitcoin",
                done = completedChapters[3] ?: false,
                onClick = { navController.navigate(route = "${Screen.ChapterScreen.route}/3") }
            )

            SectionTitle("Transactions", false)
            SectionDivider()
            TutorialCard(
                title = "4. What is the mempool?",
                done = completedChapters[4] ?: false,
                onClick = { navController.navigate(route = "${Screen.ChapterScreen.route}/4") }
            )
            TutorialCard(
                title = "5. What are transaction fees?",
                done = completedChapters[5] ?: false,
                onClick = { navController.navigate(route = "${Screen.ChapterScreen.route}/5") }
            )
            TutorialCard(
                title = "6. Bitcoin units",
                done = completedChapters[6] ?: false,
                onClick = { navController.navigate(route = "${Screen.ChapterScreen.route}/6") }
            )

            SectionTitle("Wallets", false)
            SectionDivider()
            TutorialCard(
                title = "7. What is a recovery phrase?",
                done = completedChapters[7] ?: false,
                onClick = { navController.navigate(route = "${Screen.ChapterScreen.route}/7") }
            )
            TutorialCard(
                title = "8. Recovering your wallet",
                done = completedChapters[8] ?: false,
                onClick = { navController.navigate(route = "${Screen.ChapterScreen.route}/8") }
            )
            TutorialCard(
                title = "9. The different types of wallets",
                done = completedChapters[9] ?: false,
                onClick = { navController.navigate(route = "${Screen.ChapterScreen.route}/9") }
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, first: Boolean) {
    Text(
        modifier = if (first) Modifier.padding(top = 16.dp, start = 4.dp) else Modifier.padding(top = 42.dp, start = 4.dp),
        text = title,
        style = TextStyle(
            fontFamily = Outfit,
            color = padawan_theme_text_headline,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    )
}

@Composable
fun SectionDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 8.dp, start = 4.dp, end = 4.dp),
        color = Color.Black,
        thickness = 2.dp
    )
}
