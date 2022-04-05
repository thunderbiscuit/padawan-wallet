package com.goldenraven.padawanwallet.ui.tutorials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.theme.md_theme_dark_background

@Composable
fun TutorialsScreen(tutorial: Tutorial) {
    var title = ""
    var content: List<List<String>> = listOf(listOf())
    when (tutorial) {
        Tutorial.T1 -> {
            title = stringResource(R.string.E1_title)
            content = listOf(
                listOf("h1", stringResource(R.string.t1_h1)),
                listOf("p1", stringResource(R.string.t1_p1)),
                listOf("h2", stringResource(R.string.t1_h2)),
                listOf("p2", stringResource(R.string.t1_p2)),
                listOf("h3", stringResource(R.string.t1_h3)),
                listOf("p3", stringResource(R.string.t1_p3)),
                listOf("h4", stringResource(R.string.t1_h4)),
                listOf("p4", stringResource(R.string.t1_p4))
            )
        }
        Tutorial.T2 -> {
            title = stringResource(R.string.E2_title)
            content = listOf(
                listOf("h1", stringResource(R.string.t2_h1)),
                listOf("p1", stringResource(R.string.t2_p1)),
                listOf("p2", stringResource(R.string.t2_p2)),
                listOf("p3", stringResource(R.string.t2_p2)),
                listOf("p4", stringResource(R.string.t2_p2)),
                listOf("h2", stringResource(R.string.t2_h2)),
                listOf("p5", stringResource(R.string.t2_p5)),
                listOf("p6", stringResource(R.string.t2_p6))
            )
        }
        Tutorial.T3 -> {
            title = stringResource(R.string.E3_title)
            content = listOf(
                listOf("h1", stringResource(R.string.t3_h1)),
                listOf("p1", stringResource(R.string.t3_p1)),
                listOf("p2", stringResource(R.string.t3_p2))
            )
        }
        Tutorial.T4 -> {
            title = stringResource(R.string.E4_title)
            content = listOf(
                listOf("h1", stringResource(R.string.t4_h1)),
                listOf("p1", stringResource(R.string.t4_p1))
            )
        }
        Tutorial.T5 -> {
            title = stringResource(R.string.E5_title)
            content = listOf(
                listOf("h1", stringResource(R.string.t5_h1)),
                listOf("p1", stringResource(R.string.t5_p1))
            )
        }
        Tutorial.T6 -> {
            title = stringResource(R.string.E6_title)
            content = listOf(
                listOf("h1", stringResource(R.string.t6_h1)),
                listOf("p1", stringResource(R.string.t6_p1))
            )
        }
        Tutorial.T7 -> {
            title = stringResource(R.string.E7_title)
            content = listOf(
                listOf("h1", stringResource(R.string.t7_h1)),
                listOf("p1", stringResource(R.string.t7_p1)),
                listOf("p2", stringResource(R.string.t7_p2))
            )
        }
        Tutorial.T8 -> {
            title = stringResource(R.string.E8_title)
            content = listOf(
                listOf("h1", stringResource(R.string.t8_h1)),
                listOf("p1", stringResource(R.string.t8_p1))
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(md_theme_dark_background)
            .padding(
                all = 16.dp
            )
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .align(
                    alignment = Alignment.CenterHorizontally
                )
        )
        for (i in content) {
            if ("h" in i[0]) {
                Text(
                    text = i[1],
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(
                            alignment = Alignment.CenterHorizontally
                        )
                        .padding(
                            all = 16.dp
                        )
                )
            } else {
                Text(
                    text = i[1],
                    fontSize = 16.sp,
                )
            }
        }
    }
}

enum class Tutorial(val id: Int) {
    T1(0),
    T2(1),
    T3(2),
    T4(3),
    T5(4),
    T6(5),
    T7(6),
    T8(7);

    // we can use this to reverse-lookup the tutorial based on an Int
    companion object {
        private val map: Map<Int, Tutorial> = values().associateBy(Tutorial::id)
        fun fromId(id: Int): Tutorial? = map[id]
    }
}
