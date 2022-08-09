package com.goldenraven.padawanwallet.ui.tutorials

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.*
import com.goldenraven.padawanwallet.ui.Screen
import com.goldenraven.padawanwallet.ui.standardBorder

@Composable
internal fun TutorialsHomeScreen(tutorialViewModel: TutorialViewModel, navController: NavController) {
    val tutorialList = tutorialViewModel.tutorialsList

    val currTutorial = 0 // TODO
    val tutorial = tutorialList[currTutorial]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = padawan_theme_background_secondary)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(height = 32.dp))
            TutorialHomeTitle()
            Spacer(modifier = Modifier.height(height = 24.dp))
            TutorialHomeVisual(tutorial = tutorial)
            Spacer(modifier = Modifier.height(height = 24.dp))
            TutorialId(tutorial = tutorial)
            TutorialTitle(tutorial = tutorial)
            TutorialDesc(tutorial = tutorial)
            TutorialButton(tutorial = tutorial, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialHomeTitle() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(weight = 0.65f)) {
            Text(
                text = "Padawan journey",
                style = PadawanTypography.headlineSmall,
                color = padawan_theme_text_headline
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Text(
                text = "Continue on your journey of becoming a bitcoin master",
                style = PadawanTypography.bodyMedium,
                color = padawan_theme_text_faded
            )
        }

        Spacer(modifier = Modifier.weight(weight = 0.1f))

        Card(
            containerColor = padawan_theme_tutorial_faded,
            shape = CircleShape,
            modifier = Modifier
                .weight(weight = 0.25f)
                .aspectRatio(ratio = 1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_tutorial_star),
                contentDescription = "Tutorial Star",
                tint = padawan_theme_tutorial,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale = 0.60f)
                    .rotate(degrees = 15f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialHomeVisual(tutorial: TutorialData) {
    Card(
        containerColor = padawan_theme_tutorial_background,
        border = standardBorder,
        modifier = Modifier
            .fillMaxWidth()
            .standardShadow()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 24.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.align(alignment = Alignment.CenterStart)) {
                    Text(
                        text = "Chapter ${tutorial.id}",
                        style = PadawanTypography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(height = 8.dp))
                    Text(
                        text = tutorial.difficulty,
                        style = PadawanTypography.bodySmall
                    )
                }
                
                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterEnd)
                        .width(intrinsicSize = IntrinsicSize.Max)
                ) {
                    ProgressBar(completionPercentage = tutorial.completion.toFloat() / tutorial.data.size.toFloat())
                    Text(
                        text = "${tutorial.completion} of ${tutorial.data.size} done",
                        style = PadawanTypography.bodyMedium
                    )
                }
            }

            Box(modifier = Modifier.height(150.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Row(modifier = Modifier.align(alignment = Alignment.Center)) {
                    Icon(painter = painterResource(id = R.drawable.ic_drag_left), tint = padawan_theme_tutorial, contentDescription = "Drag Left")
                    Text(
                        text = "drag horizontally",
                        style = PadawanTypography.bodySmall,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .padding(horizontal = 16.dp)
                    )
                    Icon(painter = painterResource(id = R.drawable.ic_drag_right), tint = padawan_theme_tutorial, contentDescription = "Drag Right")
                }
            }
        }
    }
}

@Composable
fun TutorialId(tutorial: TutorialData) {
    Text(
        text = "Chapter ${tutorial.id} - ${tutorial.difficulty}",
        style = PadawanTypography.labelLarge,
        color = padawan_theme_button_primary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialTitle(tutorial: TutorialData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = tutorial.title,
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
                text = tutorial.type.lowercase(),
                style = PadawanTypography.bodySmall,
                modifier = Modifier.padding(all = 8.dp)
            )
        }
    }
}

@Composable
fun TutorialDesc(tutorial: TutorialData) {
    Text(
        text = stringResource(id = tutorial.data[0][1].resourceId),
        style = PadawanTypography.bodyMedium,
        color = padawan_theme_text_faded
    )
}

@Composable
fun TutorialButton(tutorial: TutorialData, navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { navController.navigate(route = "${Screen.TutorialsScreen.route}/${tutorial.id}") },
            colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(all = 8.dp)
                .standardShadow()
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