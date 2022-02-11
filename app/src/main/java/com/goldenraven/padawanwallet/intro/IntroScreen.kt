package com.goldenraven.padawanwallet.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.*

@Composable
internal fun IntroScreen(navController: NavController) {
    PadawanTheme {
        Column(
            modifier = Modifier
                .background(color = md_theme_light_background)
                .fillMaxSize(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            AppName()
            IntroText()
            LetsGoButton(navController)
        }
    }
}

@Composable
fun AppName() {
    Column(modifier = Modifier.padding(20.dp, 20.dp)) {
        Text(
            text = stringResource(R.string.padawan),
            color = md_theme_light_onBackground,
            fontSize = 70.sp,
            fontFamily = shareTechMono,
        )
        Text(
            stringResource(R.string.elevator_pitch),
            color = md_theme_light_onBackground,
            fontSize = 14.sp,
            fontFamily = rubik,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
internal fun IntroText() {
    Text(
        stringResource(R.string.welcome_statement),
        color = md_theme_light_onBackground,
        fontSize = 16.sp,
        fontFamily = rubik,
        fontWeight = FontWeight.Light,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}

@Composable
internal fun LetsGoButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Screen.WalletChoiceScreen.route)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = md_theme_light_primary,
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(width = 200.dp, height = 100.dp)
            .padding(top = 20.dp)
    ) {
        Text(
            stringResource(R.string.entry_button),
            color = md_theme_light_onPrimary,
            fontSize = 18.sp,
            fontFamily = rubik,
        )
    }
}

@Preview(device = Devices.PIXEL_4, showBackground = true)
@Composable
internal fun PreviewIntroScreen() {
   IntroScreen(rememberNavController())
}
