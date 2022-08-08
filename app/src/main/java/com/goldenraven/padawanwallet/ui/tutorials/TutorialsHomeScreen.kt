package com.goldenraven.padawanwallet.ui.tutorials

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = padawan_theme_background_secondary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            TutorialHomeTitle()
            TutorialHomeVisual(tutorial = tutorial)
            TutorialId(tutorial = tutorial)
            TutorialTile(tutorial = tutorial)
            TutorialDesc(tutorial = tutorial)
            TutorialButton(tutorial = tutorial, navController = navController)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialHomeTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(modifier = Modifier.weight(weight = 0.65f)) {
            // Screen Title
            Text(
                text = "Padawan journey",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            // Screen Description
            Text(
                text = "Continue on your journey of becoming a bitcoin master",
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(weight = 0.1f))

        // Screen Logo
        Card(
            containerColor = padawan_theme_tutorial_faded,
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1f)
                .weight(weight = 0.25f)
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
    // Tutorial Box
    Card(
        containerColor = padawan_theme_background_tutorial,
        border = standardBorder,
        modifier = Modifier
            .fillMaxWidth()
            .standardShadow()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxSize()
        ) {
            val (tutorialTitle, tutorialProgress, tutorialProgressVisual, dragText) = createRefs()

            // Tutorial Title & Difficulty
            Column(
                modifier = Modifier.constrainAs(tutorialTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            ) {
                Text(
                    text = "Chapter ${tutorial.id}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tutorial.difficulty,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Tutorial Progress Text & Bar TODO
            Column(
                modifier = Modifier.constrainAs(tutorialProgress) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
            ) {
                //LinearProgressIndicator(progress = tutorial.completion.toFloat() / tutorial.data.size.toFloat())
                Text(text = "${tutorial.completion} of ${tutorial.data.size} done")
            }

            // Tutorial Visual Diagram TODO
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .constrainAs(tutorialProgressVisual) {
                        top.linkTo(tutorialTitle.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            // Drag Text TODO
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .constrainAs(dragText) {
                        top.linkTo(tutorialProgressVisual.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Text(text = "drag horizontally")
            }
        }
    }
}

@Composable
fun TutorialId(tutorial: TutorialData) {
    // Tutorial ID & Difficulty
    Text(
        text = "Chapter ${tutorial.id} - ${tutorial.difficulty}",
        color = padawan_theme_tutorial_chapter,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialTile(tutorial: TutorialData) {
    // Tutorial Title & Type
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = tutorial.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(alignment = Alignment.CenterStart)
        )
        Card(
            containerColor = padawan_theme_button_primary,
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .align(alignment = Alignment.CenterEnd)
                .standardShadow()
        ) {
            Text(
                text = tutorial.type,
                modifier = Modifier.padding(all = 8.dp)
            )
        }
    }
}

@Composable
fun TutorialDesc(tutorial: TutorialData) {
    // Tutorial Description
    Text(
        text = stringResource(id = tutorial.data[0][1].resourceId),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun TutorialButton(tutorial: TutorialData, navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Start Lesson button
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
