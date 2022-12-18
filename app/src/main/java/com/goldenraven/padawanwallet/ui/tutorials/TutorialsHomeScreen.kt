package com.goldenraven.padawanwallet.ui.tutorials

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.goldenraven.padawanwallet.data.tutorial.Tutorial
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.standardBorder
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

// TODO Group composable better
// TODO Get next tutorial & page
// TODO Add scrollable box in TutorialHomeVisual + icons & lines
// TODO Update texts when new tutorials come in

@Composable
internal fun TutorialsHomeScreen(tutorialViewModel: TutorialViewModel, navController: NavController) {
    val currTutorial = 1 // TODO
    val tutorialData = remember { mutableStateOf(Tutorial(id = 0, title = "", type = "", difficulty = "", completed = false)) }
    val completedTutorialsMap = tutorialViewModel.getCompletedTutorials()
    LaunchedEffect(key1 = true) { tutorialData.value = tutorialViewModel.getTutorialData(id = currTutorial) }
    val tutorialPage = tutorialViewModel.getTutorialPages(id = currTutorial)
    val tutorialDescription = tutorialPage[0]

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
            Spacer(modifier = Modifier.height(height = 32.dp))
            TutorialHomeTitle()
            Spacer(modifier = Modifier.height(height = 24.dp))
            TutorialHomeVisual(tutorialData = tutorialData.value, tutorialPage = tutorialPage, completedTutorials = completedTutorialsMap)
            Spacer(modifier = Modifier.height(height = 24.dp))
            TutorialId(tutorialData = tutorialData.value)
            TutorialTitle(tutorialData = tutorialData.value)
            TutorialDesc(tutorialPage = tutorialDescription)
            TutorialButton(tutorialData = tutorialData.value, navController = navController)
        }
    }
}

@Composable
fun TutorialHomeTitle() {
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
fun TutorialHomeVisual(
    tutorialData: Tutorial,
    tutorialPage: List<List<TutorialElement>>,
    completedTutorials: Map<Int, Boolean>
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
                        Text(
                            text = "Chapter ${tutorialData.id}",
                            style = PadawanTypography.labelLarge
                        )
                        Spacer(modifier = Modifier.height(height = 8.dp))
                        Text(
                            text = tutorialData.difficulty,
                            style = PadawanTypography.bodySmall
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterEnd)
                            .width(intrinsicSize = IntrinsicSize.Max)
                    ) {
                        ProgressBar(completionPercentage = currentPage.toFloat() / tutorialPage.size.toFloat())
                        Text(
                            text = "${tutorialData.completed} of ${tutorialPage.size} done",
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
                            val completed = completedTutorials[it] ?: false
                            Log.i("TutorialsHomeScreen", "Completed variable was $completed")
                            LessonCircle(lessonNumber = it, completed = completed)
                            // LessonCircle(lessonNumber = it, completed = completedTutorials[it] ?: false)
                        }
                        1 -> (4..7).forEach {
                            LessonCircle(lessonNumber = it, completed = false)
                            // LessonCircle(lessonNumber = it, completed = completedTutorials[it] ?: false)
                        }
                        2 -> (8..10).forEach {
                            LessonCircle(lessonNumber = it, completed = false)
                        }
                    }
                    // LessonCircle(lessonNumber = 1, completed = false)
                    // LessonCircle(lessonNumber = 2, completed = true)
                    // LessonCircle(lessonNumber = 3, completed = false)
                }
            }
        }
    }
}

fun getBackgroundColor(page: Int): Color {
    return when (page) {
        0 -> Color(0xffba4ffc)
        1 -> Color(0xfffc4f79)
        2 -> Color(0xffff6d3b)
        else -> Color(0xffffffff)
    }
}

@Composable
fun LessonCircle(lessonNumber: Int, completed: Boolean) {
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
                    radius = 82f,
                )
                drawCircle(
                    color = Color(0xff000000),
                    radius = 64f
                )
            }
            .clickable {
                Log.i("Tutorials", "Clicked on tutorial $lessonNumber")
            },
    )
}

@Composable
fun TutorialId(tutorialData: Tutorial) {
    Text(
        modifier = Modifier.padding(horizontal = 32.dp),
        text = "Chapter ${tutorialData.id} - ${tutorialData.difficulty}",
        style = PadawanTypography.labelLarge,
        color = padawan_theme_button_primary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialTitle(tutorialData: Tutorial) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .padding(horizontal = 32.dp)
    ) {
        Text(
            text = tutorialData.title,
            style = PadawanTypography.headlineSmall,
            fontSize = 20.sp,
            modifier = Modifier.align(alignment = Alignment.CenterStart)
        )
        Card(
            containerColor = padawan_theme_button_primary,
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier.align(alignment = Alignment.CenterEnd)
        ) {
            Text(
                text = tutorialData.type.lowercase(),
                style = PadawanTypography.bodySmall,
                modifier = Modifier.padding(all = 8.dp)
            )
        }
    }
}

@Composable
fun TutorialDesc(tutorialPage: List<TutorialElement>) {
    Text(
        modifier = Modifier.padding(horizontal = 32.dp),
        text = stringResource(id = tutorialPage[0].resourceId),
        style = PadawanTypography.bodyMedium,
        color = padawan_theme_text_faded_secondary
    )
}

@Composable
fun TutorialButton(tutorialData: Tutorial, navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { navController.navigate(route = "${Screen.TutorialsScreen.route}/${tutorialData.id}") },
            colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(all = 8.dp)
                .standardShadow(20.dp)
                .align(alignment = Alignment.Center),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Start Lesson",
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
