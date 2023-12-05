package com.goldenraven.padawanwallet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.Outfit
import com.goldenraven.padawanwallet.theme.padawan_theme_background
import com.goldenraven.padawanwallet.theme.padawan_theme_onBackground_secondary
import com.goldenraven.padawanwallet.theme.standardShadow

@Composable
fun TutorialCard(
    title: String,
    done: Boolean,
    onClick: () -> Unit,
) {
    // if (done) {
        Card(
            border = standardBorder,
            shape = RoundedCornerShape(20.dp),
            colors = if (done) CardDefaults.cardColors(padawan_theme_background) else CardDefaults.cardColors(
                padawan_theme_onBackground_secondary
            ),
            modifier = Modifier
                .padding(vertical = 4.dp)
                .standardShadow(20.dp)
                .height(70.dp)
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(all = 16.dp),
                    style = TextStyle(
                        fontFamily = Outfit,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                    ),
                )
                Image(
                    painter = if (done) painterResource(id = R.drawable.ic_hicon_star) else painterResource(
                        id = R.drawable.ic_hicon_circle
                    ),
                    contentDescription = "Done",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 16.dp)
                )
            }
        }
    // } else {
    //     Card(
    //         border = standardBorder,
    //         shape = RoundedCornerShape(20.dp),
    //         modifier = Modifier
    //             .padding(vertical = 4.dp)
    //             .standardShadow(20.dp)
    //             .height(70.dp)
    //             .fillMaxWidth()
    //     ) {
    //         Row(
    //             modifier = Modifier.fillMaxSize().background(tutorialGradient),
    //             verticalAlignment = Alignment.CenterVertically,
    //             horizontalArrangement = Arrangement.SpaceBetween
    //         ) {
    //             Text(
    //                 text = title,
    //                 modifier = Modifier.padding(all = 16.dp),
    //                 style = TextStyle(
    //                     fontFamily = Outfit,
    //                     fontWeight = FontWeight.Medium,
    //                     fontSize = 18.sp,
    //                 ),
    //             )
    //             Image(
    //                 painter = if (done) painterResource(id = R.drawable.ic_hicon_star) else painterResource(
    //                     id = R.drawable.ic_hicon_circle
    //                 ),
    //                 contentDescription = "Done",
    //                 modifier = Modifier
    //                     .size(48.dp)
    //                     .padding(end = 16.dp)
    //             )
    //         }
    //     }
    // }
}

val tutorialGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF76dab3),
        Color(0xFF76dab3),
        Color(0xFFf3f4ff)
    ),
    start = Offset(0f, 0f),
    end = Offset(750f, 0f)
)
