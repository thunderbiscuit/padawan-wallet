/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens.lessons

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
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.domain.settings.PadawanColorTheme
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanColorsTatooineDesert
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTypography
import com.coyotebitcoin.padawanwallet.presentation.ui.components.SectionDivider
import com.coyotebitcoin.padawanwallet.presentation.ui.components.SectionTitle
import com.coyotebitcoin.padawanwallet.presentation.ui.components.LessonCard
import com.coyotebitcoin.padawanwallet.presentation.utils.ScreenSizeWidth
import com.coyotebitcoin.padawanwallet.presentation.utils.getScreenSizeWidth
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.LessonsRootScreenState

private const val TAG = "PadawanTag/ChaptersRootScreen"

@Composable
internal fun LessonsRootScreen(
    state: LessonsRootScreenState,
    onChapterNav: (Int) -> Unit,
    paddingValues: PaddingValues,
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
            LessonCard(
                title = "1. ${stringResource(id = R.string.l1_title)}",
                done = state.completedLessons[1] ?: false,
                onClick = {
                    onChapterNav(1)
                }
            )
            LessonCard(
                title = "2. ${stringResource(id = R.string.l2_title)}",
                done = state.completedLessons[2] ?: false,
                onClick = {
                    onChapterNav(2)
                }
            )
            LessonCard(
                title = "3. ${stringResource(id = R.string.l3_title)}",
                done = state.completedLessons[3] ?: false,
                onClick = {
                    onChapterNav(3)
                }
            )

            SectionTitle("Transactions", false)
            SectionDivider()
            LessonCard(
                title = "4. ${stringResource(id = R.string.l4_title)}",
                done = state.completedLessons[4] ?: false,
                onClick = {
                    onChapterNav(4)
                }
            )
            LessonCard(
                title = "5. ${stringResource(id = R.string.l5_title)}",
                done = state.completedLessons[5] ?: false,
                onClick = {
                    onChapterNav(5)
                }
            )
            LessonCard(
                title = "6. ${stringResource(id = R.string.l6_title)}",
                done = state.completedLessons[6] ?: false,
                onClick = {
                    onChapterNav(6)
                }
            )

            SectionTitle("Wallets", false)
            SectionDivider()
            LessonCard(
                title = "7. ${stringResource(id = R.string.l7_title)}",
                done = state.completedLessons[7] ?: false,
                onClick = {
                    onChapterNav(7)
                }
            )
            LessonCard(
                title = "8. ${stringResource(id = R.string.l8_title)}",
                done = state.completedLessons[8] ?: false,
                onClick = {
                    onChapterNav(8)
                }
            )
            LessonCard(
                title = "9. ${stringResource(id = R.string.l9_title)}",
                done = state.completedLessons[9] ?: false,
                onClick = {
                    onChapterNav(9)
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
        LessonsRootScreen(
            state = LessonsRootScreenState(),
            onChapterNav = {},
            paddingValues = PaddingValues(0.dp),
        )
    }
}
