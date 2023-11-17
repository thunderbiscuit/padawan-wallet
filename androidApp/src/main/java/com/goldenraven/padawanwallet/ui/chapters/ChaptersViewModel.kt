/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE.txt file.
 */

package com.goldenraven.padawanwallet.ui.chapters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.goldenraven.padawanwallet.data.tutorials.TutorialRepository
import com.goldenraven.padawanwallet.data.tutorials.ChapterElement
import com.goldenraven.padawanwallet.data.tutorials.chapter1
import com.goldenraven.padawanwallet.data.tutorials.chapter2
import com.goldenraven.padawanwallet.data.tutorials.chapter3
import com.goldenraven.padawanwallet.data.tutorials.chapter4
import com.goldenraven.padawanwallet.data.tutorials.chapter5
import com.goldenraven.padawanwallet.data.tutorials.chapter6
import com.goldenraven.padawanwallet.data.tutorials.chapter7
import com.goldenraven.padawanwallet.data.tutorials.chapter8
import com.goldenraven.padawanwallet.data.tutorials.chapter9
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "ChaptersViewModel"

class ChaptersViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefTutorialsRepository: TutorialRepository = TutorialRepository
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

    fun setCompleted(index: Int) {
        viewModelScope.launch {
            sharedPrefTutorialsRepository.setCompletedTutorial(index)
        }
    }

    fun unsetAllCompleted() {
        viewModelScope.launch {
            sharedPrefTutorialsRepository.unsetAllCompleted()
        }
    }

    fun getCompletedChapters(): Map<Int, Boolean> {
        var completedTutorials: Map<Int, Boolean>
        runBlocking {
            completedTutorials = sharedPrefTutorialsRepository.getCompletedTutorials()
        }
        return completedTutorials.toMap()
    }

    fun getSpecificTutorialPages(id: Int): List<List<ChapterElement>> {
        return chapterPageMap.get(key = id) ?: throw Exception("Tutorial with id $id does not exist")
    }
}
