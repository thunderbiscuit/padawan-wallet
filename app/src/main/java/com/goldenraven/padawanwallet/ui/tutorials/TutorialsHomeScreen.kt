package com.goldenraven.padawanwallet.ui.tutorials

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.md_theme_dark_background
import com.goldenraven.padawanwallet.theme.md_theme_dark_background2
import com.goldenraven.padawanwallet.ui.Screen

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
internal fun TutorialsHomeScreen(navController: NavController) {

    val tutorialsNameList = arrayListOf(
        listOf(stringResource(R.string.E1_title), stringResource(R.string.concept)),
        listOf(stringResource(R.string.E2_title), stringResource(R.string.concept)),
        listOf(stringResource(R.string.E3_title), stringResource(R.string.skill)),
        listOf(stringResource(R.string.E4_title), stringResource(R.string.concept)),
        listOf(stringResource(R.string.E5_title), stringResource(R.string.skill)),
        listOf(stringResource(R.string.E6_title), stringResource(R.string.concept)),
        listOf(stringResource(R.string.E7_title), stringResource(R.string.concept)),
        listOf(stringResource(R.string.E8_title), stringResource(R.string.skill))
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(md_theme_dark_background)
    ) {
        itemsIndexed(tutorialsNameList) { index, item ->
            Card(
                shape = RoundedCornerShape(8.dp),
                containerColor = md_theme_dark_background2,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clickable(onClick = {
                        navController.navigate(Screen.TutorialsScreen.route + "/$index")
                    })
            ) {
                Text(
                    text = item[0],
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(
                            alignment = Alignment.CenterHorizontally
                        )
                        .padding(
                            top = 16.dp,
                        )
                )
                Text(
                    text = item[1],
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(
                            alignment = Alignment.CenterHorizontally
                        )
                        .padding(
                            bottom = 16.dp,
                        )
                )
            }
        }
    }
}