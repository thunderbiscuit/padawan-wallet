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
import com.coyotebitcoin.padawanwallet.domain.tutorials.TutorialRepository
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.ChaptersRootState
import com.coyotebitcoin.padawanwallet.presentation.viewmodels.mvi.ChaptersScreensAction
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "ChaptersViewModel"

class ChaptersViewModel : ViewModel() {
    private val initialCompletedChaptersState: Map<Int, Boolean>

    init {
        val completedChapters: Map<Int, Boolean>
        runBlocking {
            completedChapters = TutorialRepository.getCompletedTutorials()
        }
        initialCompletedChaptersState = completedChapters
    }

    var rootState: ChaptersRootState by mutableStateOf(ChaptersRootState(initialCompletedChaptersState))
        private set

    fun onAction(action: ChaptersScreensAction) {
        when (action) {
            is ChaptersScreensAction.SetCompleted     -> setCompleted(action.chapterNum)
            is ChaptersScreensAction.ResetAllChapters -> resetCompletedLessons()
        }
    }

    private fun setCompleted(chapterNum: Int) {
        var completedChapters: Map<Int, Boolean>
        runBlocking {
            TutorialRepository.setCompletedTutorial(chapterNum)
            completedChapters = TutorialRepository.getCompletedTutorials()
        }
        rootState = ChaptersRootState(completedChapters.toMap())
    }

    private fun resetCompletedLessons() {
        viewModelScope.launch {
            TutorialRepository.resetCompletedLessons()
        }
        rootState = ChaptersRootState()
    }
}
