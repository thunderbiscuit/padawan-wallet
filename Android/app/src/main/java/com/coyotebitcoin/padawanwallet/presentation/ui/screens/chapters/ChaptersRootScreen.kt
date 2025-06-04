/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens.chapters

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
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.domain.settings.PadawanColorTheme
import com.coyotebitcoin.padawanwallet.presentation.navigation.ChapterScreen
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanColorsTatooineDesert
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTypography
import com.coyotebitcoin.padawanwallet.presentation.ui.components.SectionDivider
import com.coyotebitcoin.padawanwallet.presentation.ui.components.SectionTitle
import com.coyotebitcoin.padawanwallet.presentation.ui.components.TutorialCard
import com.coyotebitcoin.padawanwallet.presentation.utils.ScreenSizeWidth
import com.coyotebitcoin.padawanwallet.presentation.utils.getScreenSizeWidth
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.ChaptersRootState
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.ChaptersScreensAction

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
            .background(PadawanColorsTatooineDesert.background)
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
                title = "1. ${stringResource(id = R.string.l1_title)}",
                done = state.completedChapters[1] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(1))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "2. ${stringResource(id = R.string.l2_title)}",
                done = state.completedChapters[2] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(2))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "3. ${stringResource(id = R.string.l3_title)}",
                done = state.completedChapters[3] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(3))
                    navController.navigate(ChapterScreen)
                }
            )

            SectionTitle("Transactions", false)
            SectionDivider()
            TutorialCard(
                title = "4. ${stringResource(id = R.string.l4_title)}",
                done = state.completedChapters[4] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(4))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "5. ${stringResource(id = R.string.l5_title)}",
                done = state.completedChapters[5] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(5))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "6. ${stringResource(id = R.string.l6_title)}",
                done = state.completedChapters[6] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(6))
                    navController.navigate(ChapterScreen)
                }
            )

            SectionTitle("Wallets", false)
            SectionDivider()
            TutorialCard(
                title = "7. ${stringResource(id = R.string.l7_title)}",
                done = state.completedChapters[7] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(7))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "8. ${stringResource(id = R.string.l8_title)}",
                done = state.completedChapters[8] ?: false,
                onClick = {
                    onAction(ChaptersScreensAction.OpenChapter(8))
                    navController.navigate(ChapterScreen)
                }
            )
            TutorialCard(
                title = "9. ${stringResource(id = R.string.l9_title)}",
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
    PadawanTheme(PadawanColorTheme.TATOOINE_DESERT) {
        ChaptersRootScreen(
            state = ChaptersRootState(),
            onAction = {},
            paddingValues = PaddingValues(0.dp),
            navController = rememberNavController()
        )
    }
}
