/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.tutorials

import com.goldenraven.padawanwallet.R

data class TutorialElement(val elementType: ElementType, val resourceId: Int)

typealias Page = List<TutorialElement>

enum class ElementType {
    TITLE,
    SUBTITLE,
    BODY,
    RESOURCE,
}

val tutorial1: List<Page> = listOf(
    listOf(
        TutorialElement(ElementType.BODY, R.string.C1_tagline)
    ),
    listOf(
        TutorialElement(ElementType.TITLE, R.string.C1_title),
        TutorialElement(ElementType.BODY, R.string.t1_p1),
        TutorialElement(ElementType.BODY, R.string.t1_p2),
    ),
    listOf(
        TutorialElement(ElementType.SUBTITLE, R.string.t1_h2),
        TutorialElement(ElementType.BODY, R.string.t1_p3),
        TutorialElement(ElementType.BODY, R.string.t1_p4),
    ),
    listOf(
        TutorialElement(ElementType.SUBTITLE, R.string.t1_h3),
        TutorialElement(ElementType.BODY, R.string.t1_p5),
        TutorialElement(ElementType.BODY, R.string.t1_p6),
    ),
    listOf(
        TutorialElement(ElementType.SUBTITLE, R.string.t1_h4),
        TutorialElement(ElementType.BODY, R.string.t1_p7),
        TutorialElement(ElementType.BODY, R.string.t1_p8),
        TutorialElement(ElementType.BODY, R.string.t1_p9),
    )
)

val tutorial2: List<Page> = listOf(
    listOf(
        TutorialElement(ElementType.BODY, R.string.C2_tagline)
    ),
    listOf(
        TutorialElement(ElementType.TITLE, R.string.C2_title),
        TutorialElement(ElementType.BODY, R.string.t2_p1),
        TutorialElement(ElementType.BODY, R.string.t2_p2),
        TutorialElement(ElementType.BODY, R.string.t2_p3),
        TutorialElement(ElementType.RESOURCE, R.drawable.placeholder_image),
        TutorialElement(ElementType.BODY, R.string.t2_p4),
        TutorialElement(ElementType.BODY, R.string.t2_p5),
        TutorialElement(ElementType.RESOURCE, R.drawable.placeholder_image),
        TutorialElement(ElementType.BODY, R.string.t2_p6),
    ),
    listOf(
        TutorialElement(ElementType.SUBTITLE, R.string.t2_h1),
        TutorialElement(ElementType.BODY, R.string.t2_p7),
        TutorialElement(ElementType.BODY, R.string.t2_p8),
        TutorialElement(ElementType.BODY, R.string.t2_p9),
        TutorialElement(ElementType.BODY, R.string.t2_p10),
    ),
    listOf(
        TutorialElement(ElementType.SUBTITLE, R.string.t2_h2),
        TutorialElement(ElementType.BODY, R.string.t2_p11),
        TutorialElement(ElementType.BODY, R.string.t2_p12),
        TutorialElement(ElementType.BODY, R.string.t2_p13),
)
)

val tutorial3: List<Page> = listOf(
    listOf(
        TutorialElement(ElementType.BODY, R.string.C3_tagline)
    ),
    listOf(
        TutorialElement(ElementType.TITLE, R.string.C3_title),
        TutorialElement(ElementType.BODY, R.string.t3_p1),
        TutorialElement(ElementType.BODY, R.string.t3_p2),
        TutorialElement(ElementType.RESOURCE, R.drawable.placeholder_image),
    )
)

val tutorial4: List<Page> = listOf(
    listOf(
        TutorialElement(ElementType.BODY, R.string.C4_tagline)
    ),
    listOf(
        TutorialElement(ElementType.TITLE, R.string.C4_title),
        TutorialElement(ElementType.BODY, R.string.t4_p1),
        TutorialElement(ElementType.BODY, R.string.t4_p2),
    ),
    listOf(
        TutorialElement(ElementType.SUBTITLE, R.string.t4_h1),
        TutorialElement(ElementType.BODY, R.string.t4_p3),
        TutorialElement(ElementType.BODY, R.string.t4_p4),
    ),
    listOf(
        TutorialElement(ElementType.SUBTITLE, R.string.t4_h2),
        TutorialElement(ElementType.BODY, R.string.t4_p5),
        TutorialElement(ElementType.BODY, R.string.t4_p6),
    )
)

val tutorial6: List<Page> = listOf(
    listOf(
        TutorialElement(ElementType.BODY, R.string.C5_tagline)
    ),
    listOf(
        TutorialElement(ElementType.TITLE, R.string.C5_title),
        TutorialElement(ElementType.BODY, R.string.t2_p1),
    )
)

val tutorial5: List<Page> = listOf(
    listOf(
        TutorialElement(ElementType.BODY, R.string.C6_tagline)
    ),
    listOf(
        TutorialElement(ElementType.TITLE, R.string.C5_title),
        TutorialElement(ElementType.BODY, R.string.t6_p1),
    )
)

val tutorial7: List<Page> = listOf(
    listOf(
        TutorialElement(ElementType.BODY, R.string.C7_tagline)
    ),
    listOf(
        TutorialElement(ElementType.TITLE, R.string.C7_title),
        TutorialElement(ElementType.BODY, R.string.t7_p1),
        TutorialElement(ElementType.BODY, R.string.t7_p2),
    )
)

val tutorial8: List<Page> = listOf(
    listOf(
        TutorialElement(ElementType.BODY, R.string.C8_tagline)
    ),
    listOf(
        TutorialElement(ElementType.TITLE, R.string.C8_title),
        TutorialElement(ElementType.BODY, R.string.t8_p1),
    )
)

val tutorial9: List<Page> = listOf(
    listOf(
        TutorialElement(ElementType.BODY, R.string.C9_tagline)
    ),
    listOf(
        TutorialElement(ElementType.SUBTITLE, R.string.t9_h1),
        TutorialElement(ElementType.BODY, R.string.t9_p1),
    )
)
