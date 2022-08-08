/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.tutorials

import com.goldenraven.padawanwallet.R

data class TutorialData(
    val id: Int,
    val title: String,
    val type: String,
    val difficulty: String,
    val completion: Int,
    val data: List<List<TutorialElement>>
)

data class TutorialElement(val elementType: ElementType, val resourceId: Int)

enum class ElementType {
    TITLE,
    SUBTITLE,
    BODY,
    RESOURCE,
}

enum class Tutorial(val id: Int, val data: List<List<TutorialElement>>) {
    T1(id = 1, data = tutorial1),
    T2(id = 2, data = tutorial2),
    T3(id = 3, data = tutorial3),
    T4(id = 4, data = tutorial4),
    T5(id = 5, data = tutorial5),
    T6(id = 6, data = tutorial6),
    T7(id = 7, data = tutorial7),
    T8(id = 8, data = tutorial8);

    // we can use this to reverse-lookup the tutorial based on an Int
    companion object {
        private val map: Map<Int, Tutorial> = values().associateBy(Tutorial::id)
        fun fromId(id: Int): Tutorial? = map[id]
    }
}

val tutorial1: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E1_title),
        TutorialElement(ElementType.BODY, R.string.t1_p1),
        TutorialElement(ElementType.SUBTITLE, R.string.t1_h2),
    ),
    listOf(
        TutorialElement(ElementType.BODY, R.string.t1_p2),
        TutorialElement(ElementType.SUBTITLE, R.string.t1_h3),
    ),
    listOf(
        TutorialElement(ElementType.BODY, R.string.t1_p3),
        TutorialElement(ElementType.SUBTITLE, R.string.t1_h4),
        TutorialElement(ElementType.BODY, R.string.t1_p4),
    )
)

val tutorial2: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E2_title),
        TutorialElement(ElementType.BODY, R.string.t2_p1),
        TutorialElement(ElementType.BODY, R.string.t2_p2),
        TutorialElement(ElementType.BODY, R.string.t2_p3),
    ),
    listOf(
        TutorialElement(ElementType.BODY, R.string.t2_p4),
        TutorialElement(ElementType.RESOURCE, R.drawable.placeholder_image),
        TutorialElement(ElementType.BODY, R.string.t2_h2),
        TutorialElement(ElementType.BODY, R.string.t2_p5),
        TutorialElement(ElementType.BODY, R.string.t2_p6),
    )
)

val tutorial3: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E3_title),
        TutorialElement(ElementType.BODY, R.string.t3_p1),
        TutorialElement(ElementType.BODY, R.string.t3_p2),
    )
)

val tutorial4: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E4_title),
        TutorialElement(ElementType.BODY, R.string.t4_p1),
    )
)

val tutorial5: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E5_title),
        TutorialElement(ElementType.BODY, R.string.t5_p1),
    )
)

val tutorial6: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E6_title),
        TutorialElement(ElementType.BODY, R.string.t6_p1),
    )
)

val tutorial7: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E7_title),
        TutorialElement(ElementType.BODY, R.string.t7_p1),
        TutorialElement(ElementType.BODY, R.string.t7_p2),
    )
)

val tutorial8: List<List<TutorialElement>> = listOf(
    listOf(
        TutorialElement(ElementType.TITLE, R.string.E8_title),
        TutorialElement(ElementType.BODY, R.string.t8_p1),
    )
)

//val tutorial1: List<TutorialElement> = listOf(
//    TutorialElement(ElementType.TITLE, R.string.E1_title),
//    TutorialElement(ElementType.BODY, R.string.t1_p1),
//    TutorialElement(ElementType.SUBTITLE, R.string.t1_h2),
//    TutorialElement(ElementType.BODY, R.string.t1_p2),
//    TutorialElement(ElementType.SUBTITLE, R.string.t1_h3),
//    TutorialElement(ElementType.BODY, R.string.t1_p3),
//    TutorialElement(ElementType.SUBTITLE, R.string.t1_h4),
//    TutorialElement(ElementType.BODY, R.string.t1_p4),
//)
//
//val tutorial2: List<TutorialElement> = listOf(
//    TutorialElement(ElementType.TITLE, R.string.E2_title),
//    TutorialElement(ElementType.BODY, R.string.t2_p1),
//    TutorialElement(ElementType.BODY, R.string.t2_p2),
//    TutorialElement(ElementType.BODY, R.string.t2_p3),
//    TutorialElement(ElementType.BODY, R.string.t2_p4),
//    TutorialElement(ElementType.RESOURCE, R.drawable.placeholder_image),
//    TutorialElement(ElementType.BODY, R.string.t2_h2),
//    TutorialElement(ElementType.BODY, R.string.t2_p5),
//    TutorialElement(ElementType.BODY, R.string.t2_p6),
//)
//
//val tutorial3: List<TutorialElement> = listOf(
//    TutorialElement(ElementType.TITLE, R.string.E3_title),
//    TutorialElement(ElementType.BODY, R.string.t3_p1),
//    TutorialElement(ElementType.BODY, R.string.t3_p2),
//)
//
//val tutorial4: List<TutorialElement> = listOf(
//    TutorialElement(ElementType.TITLE, R.string.E4_title),
//    TutorialElement(ElementType.BODY, R.string.t4_p1),
//)
//
//val tutorial5: List<TutorialElement> = listOf(
//    TutorialElement(ElementType.TITLE, R.string.E5_title),
//    TutorialElement(ElementType.BODY, R.string.t5_p1),
//)
//
//val tutorial6: List<TutorialElement> = listOf(
//    TutorialElement(ElementType.TITLE, R.string.E6_title),
//    TutorialElement(ElementType.BODY, R.string.t6_p1),
//)
//
//val tutorial7: List<TutorialElement> = listOf(
//    TutorialElement(ElementType.TITLE, R.string.E7_title),
//    TutorialElement(ElementType.BODY, R.string.t7_p1),
//    TutorialElement(ElementType.BODY, R.string.t7_p2),
//)
//
//val tutorial8: List<TutorialElement> = listOf(
//    TutorialElement(ElementType.TITLE, R.string.E8_title),
//    TutorialElement(ElementType.BODY, R.string.t8_p1),
//)