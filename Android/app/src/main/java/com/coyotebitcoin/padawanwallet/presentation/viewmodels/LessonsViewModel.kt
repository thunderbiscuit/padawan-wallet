/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coyotebitcoin.padawanwallet.domain.lessons.LessonsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "PTag/ChaptersViewModel"

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

class LessonsViewModel : ViewModel() {
    private val initialCompletedChaptersState: Map<Int, Boolean>

    init {
        val completedChapters: Map<Int, Boolean>
        runBlocking {
            completedChapters = LessonsRepository.getCompletedLessons()
        }
        initialCompletedChaptersState = completedChapters
    }

    val rootState: StateFlow<LessonsRootScreenState>
        field = MutableStateFlow(LessonsRootScreenState(initialCompletedChaptersState))

    fun onAction(action: LessonAction) {
        when (action) {
            is LessonAction.SetCompleted -> setCompleted(action.lessonNum)
            is LessonAction.ResetAll     -> resetCompletedLessons()
        }
    }

    private fun setCompleted(chapterNum: Int) {
        var completedChapters: Map<Int, Boolean>
        runBlocking {
            LessonsRepository.setCompletedLesson(chapterNum)
            completedChapters = LessonsRepository.getCompletedLessons()
        }
        rootState.update { it.copy(completedChapters.toMap()) }
    }

    private fun resetCompletedLessons() {
        viewModelScope.launch {
            LessonsRepository.resetCompletedLessons()
        }
        rootState.update { LessonsRootScreenState() }
    }
}
