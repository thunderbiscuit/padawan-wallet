/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.chapters

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.chapters.Chapter
import com.goldenraven.padawanwallet.data.chapters.ChapterElement
import com.goldenraven.padawanwallet.theme.PadawanTypography
import com.goldenraven.padawanwallet.theme.noRippleClickable
import com.goldenraven.padawanwallet.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.theme.padawan_theme_button_primary
import com.goldenraven.padawanwallet.theme.padawan_theme_progressbar_background
import com.goldenraven.padawanwallet.theme.padawan_theme_text_faded_secondary
import com.goldenraven.padawanwallet.theme.padawan_theme_text_headline
import com.goldenraven.padawanwallet.theme.padawan_theme_chapter_completed
import com.goldenraven.padawanwallet.theme.standardShadow
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.standardBorder
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

// TODO: Remember "next up" chapter

private const val TAG = "ChaptersRootScreen"

@Composable
internal fun ChaptersRootScreen(chaptersViewModel: ChaptersViewModel, navController: NavController) {
    val selectedChapterData: Chapter? by chaptersViewModel.selectedChapterData.collectAsState()
    val selectedChapterTagline = chaptersViewModel.getChapterPages(chaptersViewModel.selectedChapter.value)
    val defaultChapter = Chapter(0, "", "", "", false)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = padawan_theme_background_secondary)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            chaptersViewModel.updateSelectedChapter(chaptersViewModel.selectedChapter.value)

            Spacer(modifier = Modifier.height(height = 32.dp))
            ChapterHomeTitle()
            Spacer(modifier = Modifier.height(height = 24.dp))
            SectionsCarousel(viewModel = chaptersViewModel)
            Spacer(modifier = Modifier.height(height = 24.dp))
            ChapterId(chapterData = selectedChapterData ?: defaultChapter)
            ChapterTitle(chapterData = selectedChapterData ?: defaultChapter)
            ChapterTagline(chapterData = selectedChapterTagline[0])
            ChapterButton(chapterData = selectedChapterData ?: defaultChapter, navController = navController)
        }
    }
}

@Composable
fun ChapterHomeTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 0.70f)
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.padawan_journey),
                style = PadawanTypography.headlineSmall,
                color = padawan_theme_text_headline
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Text(
                text = stringResource(R.string.continue_on_your_journey),
                style = PadawanTypography.bodyMedium,
                color = padawan_theme_text_faded_secondary
            )
        }
        Spacer(modifier = Modifier.weight(weight = 0.1f))
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SectionsCarousel(
    viewModel: ChaptersViewModel
) {
    HorizontalPager(
        count = 3,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
    ) { page ->
        val cardBackgroundColor = getBackgroundColor(page)
        val screenSizeWidth = getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)

        Card(
            colors = CardDefaults.cardColors(cardBackgroundColor),
            border = standardBorder,
            modifier = Modifier
                .fillMaxWidth(0.97f)
                .standardShadow(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 24.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.align(alignment = Alignment.CenterStart)) {
                        val sectionName: String = when (page) {
                            0 -> stringResource(R.string.getting_started)
                            1 -> stringResource(R.string.transactions)
                            else -> stringResource(R.string.wallets)
                        }
                        Text(
                            text = stringResource(R.string.section, page + 1, sectionName),
                            style = PadawanTypography.labelLarge,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.height(height = 8.dp))
                    }

                    if (screenSizeWidth == ScreenSizeWidth.Phone) {
                        Column(
                            modifier = Modifier
                                .align(alignment = Alignment.CenterEnd)
                                .width(intrinsicSize = IntrinsicSize.Max)
                        ) {
                            ProgressBar(completionPercentage = calculateCompletionOfGroup(page, viewModel.getCompletedChapters()))
                            Text(
                                text = calculateCompletionStringOfGroup(
                                    page,
                                    viewModel.getCompletedChapters(),
                                    stringResource(R.string.of_3_done)
                                ),
                                style = PadawanTypography.bodyMedium
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                ) {
                    when (page) {
                        0 -> (1..3).forEach {
                            val completed = viewModel.getCompletedChapters()[it] ?: false
                            Log.i(TAG, "Completed variable was $completed")
                            val selected = (it == viewModel.selectedChapter.value)
                            LessonCircle(lessonNumber = it, completed = completed, selected = selected, selectedChapter = viewModel.selectedChapter)
                        }
                        1 -> (4..6).forEach {
                            val completed = viewModel.getCompletedChapters()[it] ?: false
                            val selected = (it == viewModel.selectedChapter.value)
                            LessonCircle(lessonNumber = it, completed = completed, selected = selected, selectedChapter = viewModel.selectedChapter)
                        }
                        2 -> (7..9).forEach {
                            val completed = viewModel.getCompletedChapters()[it] ?: false
                            val selected = (it == viewModel.selectedChapter.value)
                            LessonCircle(lessonNumber = it, completed = completed, selected = selected, selectedChapter = viewModel.selectedChapter)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonCircle(lessonNumber: Int, completed: Boolean, selected: Boolean, selectedChapter: MutableState<Int>) {
    val completedColor = if (completed) Color(0xffffc847) else Color(0xfff7e7ce)
    val screenSizeWidth = getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)
    val (innerCircleSize, outerCircleSize) = when (screenSizeWidth) {
        ScreenSizeWidth.Small -> Pair(48f, 50f)
        ScreenSizeWidth.Phone -> Pair(86f, 88f)
    }

    Text(
        text = lessonNumber.toString(),
        style = TextStyle(
            // color = completedColor,
            color = Color(0xff000000),
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
        ),
        modifier = Modifier
            .padding(24.dp)
            .drawBehind {
                drawCircle(
                    color = completedColor,
                    radius = innerCircleSize,
                )
                drawCircle(
                    color = if (selected) Color(0xff000000) else Color(0x00ffffff),
                    radius = outerCircleSize,
                    style = Stroke(width = 10f)
                )
            }
            .noRippleClickable {
                selectedChapter.value = lessonNumber
                Log.i(TAG, "Clicked on chapter $lessonNumber")
            },
    )
}

@Composable
fun ChapterId(chapterData: Chapter) {
    Text(
        modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 12.dp),
        text = stringResource(R.string.chapter, chapterData.id),
        style = PadawanTypography.labelLarge,
        color = padawan_theme_button_primary
    )
}

@Composable
fun ChapterTitle(chapterData: Chapter) {
    // TODO: I assume there is a better way to do this
    val resources = LocalContext.current.resources
    val name = "C${chapterData.id}_title"
    val chapterTitleResourceId = resources.getIdentifier(name, "string", LocalContext.current.packageName)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .padding(horizontal = 32.dp)
    ) {
        Text(
            text = stringResource(id = chapterTitleResourceId),
            style = PadawanTypography.headlineSmall,
            fontSize = 20.sp,
            modifier = Modifier.align(alignment = Alignment.CenterStart)
        )
    }
}

@Composable
fun ChapterTagline(chapterData: List<ChapterElement>) {
    Text(
        modifier = Modifier.padding(horizontal = 32.dp),
        text = stringResource(id = chapterData[0].resourceId),
        style = PadawanTypography.bodyMedium,
        color = padawan_theme_text_faded_secondary
    )
}

@Composable
fun ChapterButton(chapterData: Chapter, navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { navController.navigate(route = "${Screen.ChapterScreen.route}/${chapterData.id}") },
            colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(all = 16.dp)
                .standardShadow(20.dp)
                .align(alignment = Alignment.Center),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.start_chapter),
                    style = PadawanTypography.labelLarge,
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = stringResource(R.string.next_icon)
                )
            }
        }
    }
}

@Composable
fun ProgressBar(
    height: Dp = 12.dp,
    backgroundColor: Color = padawan_theme_progressbar_background,
    color: Color = padawan_theme_chapter_completed,
    animationDuration: Int = 1000,
    animationDelay: Int = 0,
    completionPercentage: Float,
) {
    val animateProgress = animateFloatAsState(
        targetValue = completionPercentage,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay
        )
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = height)
    ) {
        // Background
        drawLine(
            color = backgroundColor,
            cap = StrokeCap.Round,
            strokeWidth = size.height,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = 0f)
        )

        // Foreground
        if (animateProgress.value != 0f) {
            drawLine(
                color = color,
                cap = StrokeCap.Round,
                strokeWidth = size.height,
                start = Offset(x = 0f, y = 0f),
                end = Offset(x = animateProgress.value * size.width, y = 0f)
            )
        }
    }
}

fun calculateCompletionStringOfGroup(
    page: Int,
    completedChapters: Map<Int, Boolean>,
    messageString: String,
): String {
    return when (page) {
        0 -> {
            val count = completedChapters.count {
                it.key <= 3 && it.value
            }
            "$count $messageString"
        }
        1 -> {
            val count = completedChapters.count {
                it.key in 4..6 && it.value
            }
            "$count $messageString"
        }
        2 -> {
            val count = completedChapters.count {
                it.key in 7..9 && it.value
            }
            "$count $messageString"
        }
        else -> {
            Log.i(TAG, "We've made a terrible mistake")
            ""
        }
    }
}

fun calculateCompletionOfGroup(page: Int, completedChapters: Map<Int, Boolean>): Float {
    return when (page) {
        0 -> {
            completedChapters.count {
                it.key <= 3 && it.value
            }.div(3.0).toFloat()
        }
        1 -> {
            completedChapters.count {
                it.key in 4..6 && it.value
            }.div(3.0).toFloat()
        }
        2 -> {
            completedChapters.count {
                it.key in 7..9 && it.value
            }.div(3.0).toFloat()
        }
        else -> {
            Log.i(TAG, "We've made a terrible mistake")
            1.0.toFloat()
        }
    }
}

fun getBackgroundColor(page: Int): Color {
    // TODO #3: Consider softening these colours
    // All 3 original colours are saturated at 97
    // The new ones are saturated at 90
    return when (page) {
        0 -> Color(0xffb855f6)
        1 -> Color(0xfff6557e)
        2 -> Color(0xfff57247)
        else -> Color(0xffffffff)
    }
}
