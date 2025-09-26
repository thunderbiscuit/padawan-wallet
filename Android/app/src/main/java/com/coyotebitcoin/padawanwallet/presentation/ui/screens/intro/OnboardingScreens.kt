/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.ui.screens.intro

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.GraduationCap
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ThumbsUp
import com.coyotebitcoin.padawanwallet.R
import com.coyotebitcoin.padawanwallet.domain.utils.WalletCreateType
import com.coyotebitcoin.padawanwallet.presentation.navigation.SecondaryDestinations
import com.coyotebitcoin.padawanwallet.presentation.theme.LocalPadawanColors
import com.coyotebitcoin.padawanwallet.presentation.theme.neuBrutalismShadow
import com.coyotebitcoin.padawanwallet.presentation.ui.components.standardBorder
import kotlinx.coroutines.launch

private const val TAG = "OnboardingScreens"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreens(
    onBuildWallet: (WalletCreateType) -> Unit,
    onCreateNav: (SecondaryDestinations) -> Unit,
    onRecoverNav: () -> Unit,
) {
    val colors = LocalPadawanColors.current

    val pages = listOf(
        OnboardingPage(
            title = stringResource(id = R.string.onboard_title_1),
            description = stringResource(id = R.string.onboard_1),
            drawableRes = R.drawable.padawan_icon_bw
        ),
        OnboardingPage(
            title = stringResource(id = R.string.onboard_title_2),
            description = stringResource(id = R.string.onboard_2),
            icon = Icons.Rounded.CurrencyBitcoin
        ),
        OnboardingPage(
            title = stringResource(id = R.string.onboard_title_3),
            description = stringResource(id = R.string.onboard_3),
            icon = Lucide.GraduationCap
        )
    )

    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pager content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            if (page < 3) {
                OnboardingPageContent(
                    page = pages[page],
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                CreateWalletPage(onBuildWallet, onCreateNav, onRecoverNav)
            }
        }

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage > 0 && pagerState.currentPage < 3) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Lucide.ChevronLeft,
                        contentDescription = "Previous page",
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 16.dp else 12.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color.Black
                                else Color.Black.copy(alpha = 0.3f)
                            )
                            .padding(horizontal = 12.dp)
                    )
                    if (index < pages.size) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            if (pagerState.currentPage < 3) {
                IconButton(
                    onClick = {
                        if (pagerState.currentPage < 4) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }
                ) {
                    if (pagerState.currentPage == 2) {
                        Icon(
                            imageVector = Lucide.ThumbsUp,
                            contentDescription = "Next page",
                        )
                    } else {
                        Icon(
                            imageVector = Lucide.ChevronRight,
                            contentDescription = "Next page",
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    }
}

@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    val colors = LocalPadawanColors.current
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .size(160.dp)
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.background2
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (page.drawableRes != null) {
                    Image(
                        painter = painterResource(id = page.drawableRes),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                    )
                } else if (page.icon != null) {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Black
                    )
                }
            }
        }

        // Title
        Text(
            text = page.title,
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Description
        Text(
            text = page.description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun CreateWalletPage(
    onBuildWallet: (WalletCreateType) -> Unit,
    onCreateNav: (SecondaryDestinations) -> Unit,
    onRecoverNav: () -> Unit,
) {
    val colors = LocalPadawanColors.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.padawan_icon_bw),
            contentDescription = null,
            modifier = Modifier.size(180.dp),
        )

        Spacer(modifier = Modifier.padding(20.dp))

        Button(
            onClick = {
                Log.i(TAG, "Creating a wallet")
                onBuildWallet(WalletCreateType.FROMSCRATCH)
                onCreateNav(SecondaryDestinations.CoreScreens)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accent2,
            ),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
                .neuBrutalismShadow()
                .height(120.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(R.string.create_a_wallet),
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = { onRecoverNav() },
            colors = ButtonDefaults.buttonColors(containerColor = colors.accent2),
            shape = RoundedCornerShape(20.dp),
            border = standardBorder,
            modifier = Modifier
                .padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 4.dp)
                .neuBrutalismShadow()
                .height(120.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.already_have_a_wallet),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = colors.text
            )
        }
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector? = null,
    val drawableRes: Int? = null
)
