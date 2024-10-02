/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
 */

package com.goldenraven.padawanwallet.presentation.ui.screens.chapters

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.core.Icon
import com.composables.icons.lucide.ArrowLeft
import com.composables.icons.lucide.Lucide
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.domain.tutorials.ElementType
import com.goldenraven.padawanwallet.domain.tutorials.Page
import com.goldenraven.padawanwallet.presentation.theme.PadawanTypography
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_background_secondary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_button_primary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_button_secondary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_onPrimary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_text_faded_secondary
import com.goldenraven.padawanwallet.presentation.theme.padawan_theme_tutorial_background
import com.goldenraven.padawanwallet.presentation.theme.standardShadow
import com.goldenraven.padawanwallet.presentation.ui.components.standardBorder
import com.goldenraven.padawanwallet.utils.ScreenSizeWidth
import com.goldenraven.padawanwallet.utils.getScreenSizeWidth
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.ChaptersScreensAction
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.PageState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "ChapterScreen"

@Composable
fun ChapterScreen(
    state: PageState,
    onAction: (ChaptersScreensAction) -> Unit,
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    val pageScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = padawan_theme_background_secondary)
    ) {
        Box(
            modifier = Modifier
                .background(color = padawan_theme_tutorial_background)
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = padawan_theme_onPrimary,
                        strokeWidth = 4.dp.toPx(),
                        start = Offset(x = 0f, y = size.height),
                        end = Offset(x = size.width, y = size.height)
                    )
                }
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                ChapterAppBar(navController = navController)
                ChapterProgressBar(
                    currentPage = state.currentPage,
                    totalPages = state.totalPages
                )
            }
        }

        val screenSizeWidth = getScreenSizeWidth(LocalConfiguration.current.screenWidthDp)
        val padding = when (screenSizeWidth) {
            ScreenSizeWidth.Small -> PaddingValues(all = 16.dp)
            ScreenSizeWidth.Phone -> PaddingValues(all = 32.dp)
        }

        Column(
            modifier = Modifier
                .verticalScroll(pageScrollState)
                .padding(padding)
                .padding(bottom = paddingValues.calculateBottomPadding())
                .weight(1f),
        ) {
            ChapterPage(state.page)
            ChapterButtons(
                state = state,
                onAction = onAction,
                pageScrollState = pageScrollState,
                coroutineScope = coroutineScope,
                navController
            )
        }
    }
}

@Composable
fun ChapterButtons(
    state: PageState,
    onAction: (ChaptersScreensAction) -> Unit,
    pageScrollState: ScrollState,
    coroutineScope: CoroutineScope,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        if (!state.isLast) {
            Button(
                onClick = {
                    onAction(ChaptersScreensAction.NextPage)
                    scrollUp(pageScrollState = pageScrollState, coroutineScope = coroutineScope)
                },
                colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .standardShadow(20.dp)
                    .weight(weight = 0.5f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.next),
                        style = PadawanTypography.labelLarge,
                    )
                }
            }
        } else {
            Button(
                onClick = {
                    onAction(ChaptersScreensAction.SetCompleted)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = padawan_theme_button_primary),
                shape = RoundedCornerShape(20.dp),
                border = standardBorder,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .standardShadow(20.dp)
                    .weight(weight = 0.5f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.finish),
                        style = PadawanTypography.labelLarge,
                    )
                }
            }
        }
    }
}

fun scrollUp(pageScrollState: ScrollState, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        pageScrollState.animateScrollTo(value = 0, animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing))
    }
}

@Composable
internal fun ChapterAppBar(navController: NavHostController) {
    Row(modifier = Modifier.fillMaxWidth()) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Lucide.ArrowLeft,
                contentDescription = stringResource(R.string.back_icon),
                tint = padawan_theme_onPrimary
            )
        }
        Text(
            text = stringResource(R.string.back_to_lessons),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        )
    }
}

@Composable
fun ChapterProgressBar(
    height: Dp = 8.dp,
    spacer: Float = 30f,
    incompleteColor: Color = Color(0xfffbf5bf),
    completeColor: Color = Color(0xff1f0208),
    currentPage: Int,
    totalPages: Int,
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
            .height(height = height)
    ) {
        val progressBarLength = ((size.width + spacer) / totalPages) - spacer

        for (i in 0..< totalPages) {
            drawLine(
                color = if (currentPage >= i) completeColor else incompleteColor,
                strokeWidth = size.height,
                start = Offset(x = i * (progressBarLength + spacer), y = 0f),
                end = Offset(x = (i + 1) * (progressBarLength + spacer) - spacer, y = 0f)
            )
        }
    }
}

@Composable
internal fun ChapterPage(page: Page) {
    for (element in page) {
        when (element.elementType) {
            ElementType.TITLE -> {
                Text(
                    text = stringResource(id = element.resourceId),
                    style = PadawanTypography.headlineSmall,
                )
            }
            ElementType.SUBTITLE -> {
                Text(
                    text = stringResource(id = element.resourceId),
                    style = PadawanTypography.labelLarge,
                    fontSize = 18.sp
                )
            }
            ElementType.BODY -> {
                Text(
                    text = stringResource(id = element.resourceId),
                    style = PadawanTypography.bodyMedium,
                    color = padawan_theme_text_faded_secondary
                )
            }
            ElementType.RESOURCE -> {
                Card(
                    colors = CardDefaults.cardColors(padawan_theme_button_secondary),
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
        Spacer(modifier = Modifier.height(height = 24.dp))
    }
}
