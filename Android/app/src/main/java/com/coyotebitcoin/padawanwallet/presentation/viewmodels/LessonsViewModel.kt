/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coyotebitcoin.padawanwallet.domain.lessons.LessonsRepository
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.LessonAction
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.LessonsRootScreenState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "PTag/ChaptersViewModel"

class LessonsViewModel : ViewModel() {
    private val initialCompletedChaptersState: Map<Int, Boolean>

    init {
        val completedChapters: Map<Int, Boolean>
        runBlocking {
            completedChapters = LessonsRepository.getCompletedLessons()
        }
        initialCompletedChaptersState = completedChapters
    }

    var rootState: LessonsRootScreenState by mutableStateOf(LessonsRootScreenState(initialCompletedChaptersState))
        private set

    fun onAction(action: LessonAction) {
        when (action) {
            is LessonAction.SetCompleted     -> setCompleted(action.lessonNum)
            is LessonAction.ResetAll -> resetCompletedLessons()
        }
    }

    private fun setCompleted(chapterNum: Int) {
        var completedChapters: Map<Int, Boolean>
        runBlocking {
            LessonsRepository.setCompletedLesson(chapterNum)
            completedChapters = LessonsRepository.getCompletedLessons()
        }
        rootState = LessonsRootScreenState(completedChapters.toMap())
    }

    private fun resetCompletedLessons() {
        viewModelScope.launch {
            LessonsRepository.resetCompletedLessons()
        }
        rootState = LessonsRootScreenState()
    }
}
