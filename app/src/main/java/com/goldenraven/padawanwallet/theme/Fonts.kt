package com.goldenraven.padawanwallet.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.goldenraven.padawanwallet.R

val rubik: FontFamily = FontFamily(
    Font(R.font.rubik_regular, FontWeight.Normal),
    Font(R.font.rubik_regular_italic, FontWeight.Normal, FontStyle.Italic),

    Font(R.font.rubik_light, FontWeight.Light),
    Font(R.font.rubik_light_italic, FontWeight.Light, FontStyle.Italic)
)

val jost: FontFamily = FontFamily(
    Font(R.font.jost_regular, FontWeight.Normal),
    Font(R.font.jost_regular_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.jost_light, FontWeight.Light),
    Font(R.font.jost_light_italic, FontWeight.Light, FontStyle.Italic),
)

val shareTechMono = FontFamily(
    Font(R.font.share_tech_mono, FontWeight.Normal),
)

val nunitoSans = FontFamily(
    Font(R.font.nunito_sans, FontWeight.Normal),
    Font(R.font.nunito_sans_bold, FontWeight.Bold),
    Font(R.font.nunito_sans_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.nunito_sans_light, FontWeight.Light),
    Font(R.font.nunito_sans_light_italic, FontWeight.Light, FontStyle.Italic),
)
