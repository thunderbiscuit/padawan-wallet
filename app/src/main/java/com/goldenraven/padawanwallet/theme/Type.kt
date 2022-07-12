/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// by default
// Buttons use 			Label Large
// App Bar uses			Title Large
// Bottom bar items use Label Medium

val PadawanTypography = Typography(
	displayLarge = TextStyle(
		fontFamily = SofiaPro,
		fontWeight = FontWeight.Normal,
		fontSize = 57.sp,
		lineHeight = 64.sp,
	),
	displayMedium = TextStyle(
		fontFamily = SofiaPro,
		fontWeight = FontWeight.Normal,
		fontSize = 45.sp,
		lineHeight = 52.sp,
		letterSpacing = 0.sp,
	),
	// displaySmall = TextStyle(),

	headlineLarge = TextStyle(
		fontFamily = SofiaPro,
		fontWeight = FontWeight.Normal,
		fontSize = 32.sp,
		lineHeight = 40.sp,
		letterSpacing = 0.sp,
	),
	headlineMedium = TextStyle(
		fontFamily = SofiaPro,
		fontWeight = FontWeight.Normal,
		fontSize = 18.sp,
	),
	// headlineSmall = TextStyle(),

	titleLarge = TextStyle(
		fontFamily = SofiaPro,
		fontWeight = FontWeight.Normal,
		fontSize = 22.sp,
		lineHeight = 28.sp,
		letterSpacing = 0.sp,
	),
	// titleMedium = TextStyle(),
	// titleSmall = TextStyle(),

	bodyLarge = TextStyle(
		fontFamily = SofiaPro,
		fontWeight = FontWeight.Light,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.5.sp,
	),
	bodyMedium = TextStyle(
		fontFamily = SofiaPro,
		fontWeight = FontWeight.Light,
		color = md_theme_dark_onBackground,
		fontSize = 16.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.25.sp,
	),
	// bodySmall = TextStyle(),

	labelLarge = TextStyle(
		fontFamily = SofiaPro,
		fontWeight = FontWeight.Normal,
		fontSize = 16.sp,
	),
	labelMedium = TextStyle(
		fontFamily = SofiaPro,
		fontWeight = FontWeight.Normal,
		fontSize = 12.sp,
		lineHeight = 20.sp,
	),
	labelSmall = TextStyle(
		fontFamily = SofiaPro,
		fontWeight = FontWeight.Normal,
		fontSize = 11.sp,
		lineHeight = 16.sp,
	),
)
