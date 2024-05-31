/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraven.padawanwallet.domain.tutorials.ChapterElement
import com.goldenraven.padawanwallet.domain.tutorials.TutorialRepository
import com.goldenraven.padawanwallet.domain.tutorials.chapter1
import com.goldenraven.padawanwallet.domain.tutorials.chapter2
import com.goldenraven.padawanwallet.domain.tutorials.chapter3
import com.goldenraven.padawanwallet.domain.tutorials.chapter4
import com.goldenraven.padawanwallet.domain.tutorials.chapter5
import com.goldenraven.padawanwallet.domain.tutorials.chapter6
import com.goldenraven.padawanwallet.domain.tutorials.chapter7
import com.goldenraven.padawanwallet.domain.tutorials.chapter8
import com.goldenraven.padawanwallet.domain.tutorials.chapter9
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.ChaptersRootState
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.ChaptersScreensAction
import com.goldenraven.padawanwallet.presentation.viewmodels.mvi.PageState
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "ChaptersViewModel"

class ChaptersViewModel : ViewModel() {
    private var currentChapter = 1
    private var currentPage = 0
    private val chapterPageMap: Map<Int, List<List<ChapterElement>>> = mapOf(
        Pair(1, chapter1),
        Pair(2, chapter2),
        Pair(3, chapter3),
        Pair(4, chapter4),
        Pair(5, chapter5),
        Pair(6, chapter6),
        Pair(7, chapter7),
        Pair(8, chapter8),
        Pair(9, chapter9),
    )
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

    var pageState: PageState by mutableStateOf(PageState(page = chapter1[0], totalPages = chapter1.size))
        private set

    fun onAction(action: ChaptersScreensAction) {
        when (action) {
            is ChaptersScreensAction.SetCompleted -> setCompleted()
            is ChaptersScreensAction.ResetAllChapters -> unsetAllCompleted()
            is ChaptersScreensAction.NextPage -> nextPage()
            is ChaptersScreensAction.PreviousPage -> previousPage()
            is ChaptersScreensAction.OpenChapter -> openChapter(action.chapter)
        }
    }

    private fun openChapter(chapterIndex: Int) {
        currentChapter = chapterIndex
        currentPage = 0
        val chapter: List<List<ChapterElement>> = chapterPageMap.get(key = chapterIndex) ?: throw Exception("Chapter does not exist")
        val page: List<ChapterElement> = chapter[0]
        val isLast = chapter.size == 1
        pageState = PageState(
            page = page,
            isFirst = true,
            isLast = isLast,
            currentPage = 0,
            totalPages = chapter.size
        )
    }

    private fun setCompleted() {
        var completedChapters: Map<Int, Boolean>
        runBlocking {
            TutorialRepository.setCompletedTutorial(currentChapter)
            completedChapters = TutorialRepository.getCompletedTutorials()
        }
        rootState = ChaptersRootState(completedChapters.toMap())
    }

    private fun unsetAllCompleted() {
        viewModelScope.launch {
            TutorialRepository.unsetAllCompleted()
        }
        rootState = ChaptersRootState()
    }

    private fun nextPage() {
        currentPage++
        val chapter: List<List<ChapterElement>> = chapterPageMap.get(key = currentChapter) ?: throw Exception("Chapter does not exist")
        val page: List<ChapterElement> = chapter[currentPage]
        val isLast = currentPage == chapter.size - 1
        pageState = PageState(
            page = page,
            isFirst = false,
            isLast = isLast,
            currentPage = currentPage,
            totalPages = chapter.size,
        )
    }

    private fun previousPage() {
        currentPage--
        val chapter: List<List<ChapterElement>> = chapterPageMap.get(key = currentChapter) ?: throw Exception("Chapter does not exist")
        val page: List<ChapterElement> = chapter[currentPage]
        val isFirst = currentPage == 0
        pageState = PageState(
            page = page,
            isFirst = isFirst,
            isLast = false,
            currentPage = currentPage,
            totalPages = chapter.size,
        )
    }
}
