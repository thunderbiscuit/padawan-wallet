/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

// by default
// Buttons use 			Label Large
// App Bar uses			Title Large
// Bottom bar items use Label Medium

val PadawanTypography = Typography(
	displayLarge = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.Normal,
		fontSize = 57.sp,
		lineHeight = 64.sp,
	),
	displayMedium = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.Normal,
		fontSize = 45.sp,
		lineHeight = 52.sp,
		letterSpacing = 0.sp,
	),
	displaySmall = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.Bold,
		fontSize = 40.sp,
		lineHeight = 52.sp,
	),

	headlineLarge = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.SemiBold,
		fontSize = 32.sp,
		lineHeight = 40.sp,
		letterSpacing = 0.sp,
	),
	headlineMedium = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.SemiBold,
		fontSize = 28.sp,
		lineHeight = 36.sp,
		letterSpacing = 0.sp,
	),
	headlineSmall = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.SemiBold,
		fontSize = 24.sp,
		lineHeight = 32.sp
	),

	titleLarge = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.Normal,
		fontSize = 22.sp,
		lineHeight = 28.sp,
		letterSpacing = 0.sp,
	),
	// titleMedium = TextStyle(),
	titleSmall = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.SemiBold,
		fontSize = 18.sp,
		lineHeight = 28.sp
	),

	bodyLarge = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.Light,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.5.sp,
	),
	bodyMedium = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.Light,
		fontSize = 16.sp,
		lineHeight = 22.sp,
		letterSpacing = 0.25.sp,
	),
	bodySmall = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.Light,
		fontSize = 14.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.25.sp,
	),

	labelLarge = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.SemiBold,
		fontSize = 16.sp,
		lineHeight = 24.sp
	),
	labelMedium = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.Medium,
		fontSize = 12.sp,
		lineHeight = 20.sp,
	),
	labelSmall = TextStyle(
		fontFamily = Outfit,
		fontWeight = FontWeight.Medium,
		fontSize = 11.sp,
		lineHeight = 16.sp,
	),
)

val bodyMediumUnderlined: TextStyle = TextStyle(
	fontFamily = Outfit,
	fontWeight = FontWeight.Light,
	fontSize = 16.sp,
	lineHeight = 22.sp,
	letterSpacing = 0.25.sp,
	textDecoration = TextDecoration.Underline,
)
