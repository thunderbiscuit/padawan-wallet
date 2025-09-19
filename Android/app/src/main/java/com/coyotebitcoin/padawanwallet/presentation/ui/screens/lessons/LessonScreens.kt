/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens.lessons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.CircleCheckBig
import com.composables.icons.lucide.Lucide
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.data.ElementType
import com.coyotebitcoin.padawanwallet.data.LessonData
import com.coyotebitcoin.padawanwallet.data.Page
import com.coyotebitcoin.padawanwallet.data.chapter1
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanColorsTatooineDesert
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTheme
import com.coyotebitcoin.padawanwallet.presentation.theme.PadawanTypography
import com.coyotebitcoin.padawanwallet.presentation.theme.standardShadow
import com.coyotebitcoin.padawanwallet.presentation.ui.components.PadawanAppBar
import com.coyotebitcoin.padawanwallet.presentation.ui.components.standardBorder
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.LessonAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "PadawanTag/ChapterScreens"

@Composable
fun LessonScreens(
    lessonData: LessonData,
    onLessonDone: (LessonAction) -> Unit,
    onBack: () -> Unit,
) {
    val colors = LocalPadawanColors.current
    val (lessonNum, appBarTitleResourceId, pages) = lessonData
    val numPages = pages.size
    val pagerState = rememberPagerState(pageCount = { numPages })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            PadawanAppBar(
                title = stringResource(appBarTitleResourceId),
                onClick = { onBack() },
                color = colors.accent1
            )
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
                .background(color = colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.accent1)
                    .padding(top = 2.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(numPages) { pageIndex ->
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(52.dp)
                                .background(color = if (pagerState.currentPage >= pageIndex) colors.text else colors.background)
                        )
                    }
                }
            }
            HorizontalDivider(
                thickness = 3.dp,
                color = colors.text
            )

            // This is the main content area. It has 2 main parts:
            // 1. The pager that shows the lesson pages
            // 2. The navigation row with back/next buttons
            Column {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { pageNumber ->
                    LessonPage(pages[pageNumber])
                }

                NavigationRow(
                    coroutineScope = coroutineScope,
                    pagerState = pagerState,
                    numPages = numPages,
                    onLessonDone = { onLessonDone(LessonAction.SetCompleted(lessonNum)) },
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
fun LessonPage(page: Page) {
    val colors = LocalPadawanColors.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        for (element in page) {
            when (element.elementType) {
                ElementType.TITLE -> {
                    Text(
                        text = stringResource(id = element.resourceId),
                        style = PadawanTypography.headlineMedium,
                        color = colors.text
                    )
                }
                ElementType.SUBTITLE -> {
                    Text(
                        text = stringResource(id = element.resourceId),
                        style = PadawanTypography.headlineSmall,
                        color = colors.text
                    )
                }
                ElementType.BODY -> {
                    Text(
                        text = stringResource(id = element.resourceId),
                        style = PadawanTypography.bodyMedium,
                        color = colors.text
                    )
                }
                ElementType.RESOURCE -> {
                    Card(
                        colors = CardDefaults.cardColors(PadawanColorsTatooineDesert.accent1Light),
                        border = standardBorder,
                        modifier = Modifier
                            .height(height = 150.dp)
                            .fillMaxWidth()
                            .standardShadow(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = element.resourceId),
                                contentDescription = stringResource(R.string.image)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(height = 20.dp))
        }
    }
}

@Composable
fun NavigationRow(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    numPages: Int,
    onLessonDone: () -> Unit,
    onBack: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NavigationButton(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            },
            icon = Lucide.ChevronLeft,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        NavigationButton(
            onClick = {
                if (pagerState.currentPage < numPages - 1) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onLessonDone()
                    onBack()
                }
            },
            icon = if (pagerState.currentPage < numPages - 1) Lucide.ChevronRight else Lucide.CircleCheckBig,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun NavigationButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        border = standardBorder,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(PadawanColorsTatooineDesert.accent2),
        modifier = modifier
            .standardShadow(20.dp)
            .height(50.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Next page",
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun LessonScreensPreview() {
    val lessonData = LessonData(
        chapterNum = 1,
        appBarTitleResourceId = R.string.l1_app_bar,
        pages = chapter1
    )

    PadawanTheme {
        LessonScreens(
            lessonData = lessonData,
            onLessonDone = {},
            onBack = {},
        )
    }
}
