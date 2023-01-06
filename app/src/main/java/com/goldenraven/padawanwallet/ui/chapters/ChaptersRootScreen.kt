package com.goldenraven.padawanwallet.ui.chapters

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.standardBorder
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

// TODO: Remember "next up" chapter

private const val TAG = "ChaptersRootScreen"

@Composable
internal fun ChaptersRootScreen(chaptersViewModel: ChaptersViewModel, navController: NavController) {
    val selectedChapterData: Chapter? by chaptersViewModel.selectedChapterData.observeAsState()
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
                .weight(weight = 0.65f)
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Padawan journey",
                style = PadawanTypography.headlineSmall,
                color = padawan_theme_text_headline
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Text(
                text = "Continue on your journey of becoming a bitcoin master",
                style = PadawanTypography.bodyMedium,
                color = padawan_theme_text_faded_secondary
            )
        }
        Spacer(modifier = Modifier.weight(weight = 0.1f))
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SectionsCarousel(
    viewModel: ChaptersViewModel
) {
    HorizontalPager(
        count = 3,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
    ) { page ->
        val cardBackgroundColor = getBackgroundColor(page)

        Card(
            containerColor = cardBackgroundColor,
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
                        val sectionName: String = if (page == 0) "Getting started" else if (page == 1) "Transactions" else "Wallets"
                        Text(
                            text = "Section ${page + 1}: $sectionName",
                            style = PadawanTypography.labelLarge,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.height(height = 8.dp))
                    }

                    Column(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterEnd)
                            .width(intrinsicSize = IntrinsicSize.Max)
                    ) {
                        ProgressBar(completionPercentage = calculateCompletionOfGroup(page, viewModel.getCompletedChapters()))
                        Text(
                            text = calculateCompletionStringOfGroup(page, viewModel.getCompletedChapters()),
                            style = PadawanTypography.bodyMedium
                        )
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
    val completedColor = if (completed) Color(0xffffc847) else Color(0xcfb0b0b0)

    Text(
        text = lessonNumber.toString(),
        style = TextStyle(
            color = completedColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = ShareTechMono
        ),
        modifier = Modifier
            .padding(24.dp)
            .drawBehind {
                drawCircle(
                    color = completedColor,
                    radius = if (selected) 96f else 82f,
                )
                drawCircle(
                    color = Color(0xff000000),
                    radius = 64f
                )
            }
            .clickable {
                selectedChapter.value = lessonNumber
                Log.i(TAG, "Clicked on chapter $lessonNumber")
            },
    )
}

@Composable
fun ChapterId(chapterData: Chapter) {
    Text(
        modifier = Modifier.padding(horizontal = 32.dp),
        text = "Chapter ${chapterData.id}",
        style = PadawanTypography.labelLarge,
        color = padawan_theme_button_primary
    )
}

@Composable
fun ChapterTitle(chapterData: Chapter) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .padding(horizontal = 32.dp)
    ) {
        Text(
            text = chapterData.title,
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
                    text = "Start Chapter",
                    style = PadawanTypography.labelLarge,
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "Next Icon"
                )
            }
        }
    }
}

@Composable
fun ProgressBar(
    height: Dp = 12.dp,
    backgroundColor: Color = padawan_theme_progressbar_background,
    color: Color = padawan_theme_tutorial,
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

fun calculateCompletionStringOfGroup(page: Int, completedChapters: Map<Int, Boolean>): String {
    return when (page) {
        0 -> {
            val count = completedChapters.count {
                it.key <= 3 && it.value
            }
            "$count of 3 done"
        }
        1 -> {
            val count = completedChapters.count {
                it.key in 4..6 && it.value
            }
            "$count of 3 done"
        }
        2 -> {
            val count = completedChapters.count {
                it.key in 7..8 && it.value
            }
            "$count of 3 done"
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
        // 0 -> Color(0xffba4ffc)
        // 1 -> Color(0xfffc4f79)
        // 2 -> Color(0xffff6d3b)
        else -> Color(0xffffffff)
    }
}
