/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.chapters

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
import com.goldenraven.padawanwallet.presentation.navigation.ChapterScreen
import com.goldenraven.padawanwallet.presentation.theme.Outfit
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.innerScreenPadding
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_text_headline
import com.goldenraven.padawanwallet.presentation.ui.components.TutorialCard
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.ChaptersRootState
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.ChaptersScreensAction

private const val TAG = "ChaptersRootScreen"

@Composable
internal fun ChaptersRootScreen(
    state: ChaptersRootState,
    onAction: (ChaptersScreensAction) -> Unit,
    navController: NavController,
) {
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
                title = "1. ${stringResource(id = R.string.C1_title)}",
                done = state.completedChapters[1] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(1))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "2. ${stringResource(id = R.string.C2_title)}",
                done = state.completedChapters[2] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(2))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "3. ${stringResource(id = R.string.C3_title)}",
                done = state.completedChapters[3] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(3))
                    navController.navigate(ChapterScreen)
                }
            )

            SectionTitle("Transactions", false)
            SectionDivider()
            TutorialCard(
                title = "4. ${stringResource(id = R.string.C4_title)}",
                done = state.completedChapters[4] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(4))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "5. ${stringResource(id = R.string.C5_title)}",
                done = state.completedChapters[5] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(5))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "6. ${stringResource(id = R.string.C6_title)}",
                done = state.completedChapters[6] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(6))
                    navController.navigate(ChapterScreen)
                }
            )

            SectionTitle("Wallets", false)
            SectionDivider()
            TutorialCard(
                title = "7. ${stringResource(id = R.string.C7_title)}",
                done = state.completedChapters[7] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(7))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "8. ${stringResource(id = R.string.C8_title)}",
                done = state.completedChapters[8] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(8))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "9. ${stringResource(id = R.string.C9_title)}",
                done = state.completedChapters[9] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(9))
                    navController.navigate(ChapterScreen)
                }
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
