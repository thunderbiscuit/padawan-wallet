package com.goldenraven.padawanwallet.ui.tutorials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.theme.md_theme_dark_background

@Composable
fun TutorialsScreen(navController: NavController, tutorialId: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(md_theme_dark_background)
    ) {
        Text(text = tutorialId)
    }
}

