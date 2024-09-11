/*
 * Copyright 2021-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.viewmodels.mvi

import com.goldenraven.padawanwallet.domain.tutorials.ChapterElement

data class ChaptersRootState(
    val completedChapters: Map<Int, Boolean> = mapOf(
        Pair(1, false),
        Pair(2, false),
        Pair(3, false),
        Pair(4, false),
        Pair(5, false),
        Pair(6, false),
        Pair(7, false),
        Pair(8, false),
        Pair(9, false),
    )
)

data class PageState(
    val page: List<ChapterElement>,
    val isFirst: Boolean = true,
    val isLast: Boolean = false,
    val currentPage: Int = 0,
    val totalPages: Int,
)

sealed interface ChaptersScreensAction {
    data object SetCompleted : ChaptersScreensAction
    data object ResetAllChapters : ChaptersScreensAction
    data class OpenChapter(val chapter: Int) : ChaptersScreensAction
    data object PreviousPage : ChaptersScreensAction
    data object NextPage : ChaptersScreensAction
}
