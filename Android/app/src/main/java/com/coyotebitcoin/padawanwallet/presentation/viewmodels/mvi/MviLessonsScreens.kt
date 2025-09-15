/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi

data class LessonsRootScreenState(
    val completedLessons: Map<Int, Boolean> = mapOf(
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

sealed interface LessonAction {
    data class  SetCompleted(val lessonNum: Int) : LessonAction
    data object ResetAll : LessonAction
}
