/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.chapters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.presentation.navigation.ChapterScreen
import com.goldenraven.padawanwallet.presentation.theme.PadawanTheme
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.presentation.ui.components.SectionDivider
import com.goldenraven.padawanwallet.presentation.ui.components.SectionTitle
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
    paddingValues: PaddingValues,
    navController: NavController,
) {
    val padding = when (getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)) {
        ScreenSizeWidth.Small -> 12.dp
        ScreenSizeWidth.Phone -> 18.dp
    }

    Column(
        Modifier
            .background(padawan_theme_background_secondary)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = padding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.padawan_journey),
                style = PadawanTypography.headlineSmall,
                color = Color(0xff1f0208),
                modifier = Modifier.padding(top = 32.dp, end = 24.dp, bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.continue_on_your_journey),
                color = Color(0xff787878),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            SectionTitle(stringResource(id = R.string.getting_started), true)
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
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}

@Preview(device = Devices.PIXEL_7, showBackground = true)
@Composable
internal fun PreviewChaptersRootScreen() {
    PadawanTheme {
        ChaptersRootScreen(
            state = ChaptersRootState(),
            onAction = {},
            paddingValues = PaddingValues(0.dp),
            navController = rememberNavController()
        )
    }
}
