/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.chapters

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.chapters.*
import kotlinx.coroutines.*

private const val TAG = "ChaptersViewModel"

class ChaptersViewModel(application: Application) : AndroidViewModel(application) {
    val selectedChapter: MutableState<Int> = mutableStateOf(1)

    private val readAllData: LiveData<List<Chapter>>
    private val chapterRepository: ChapterRepository

    val selectedChapterData: MutableLiveData<Chapter> = MutableLiveData<Chapter>(Chapter(id = 0, title = "", type = "", difficulty = "", completed = false))

    private val _chapterPageMap: Map<Int, List<List<ChapterElement>>>

    private val initialChapterList = listOf(
        Chapter(id = 1, title = application.getString(R.string.C1_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completed = false),
        Chapter(id = 2, title = application.getString(R.string.C2_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completed = false),
        Chapter(id = 3, title = application.getString(R.string.C3_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.basic), completed = false),
        Chapter(id = 4, title = application.getString(R.string.C4_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completed = false),
        Chapter(id = 5, title = application.getString(R.string.C5_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.advanced), completed = false),
        Chapter(id = 6, title = application.getString(R.string.C6_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.advanced), completed = false),
        Chapter(id = 7, title = application.getString(R.string.C7_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.advanced), completed = false),
        Chapter(id = 8, title = application.getString(R.string.C8_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.advanced), completed = false),
        Chapter(id = 9, title = application.getString(R.string.C9_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.advanced), completed = false),
    )

    init {
        val chapterDao = ChaptersDatabase.getInstance(application).chapterDao()
        chapterRepository = ChapterRepository(chapterDao)
        runBlocking(Dispatchers.IO) {
            // TODO #2: this is a hack because of issue outlined in TODO #1
            val dbIsEmpty = chapterRepository.dbIsEmpty()
            Log.i(TAG, "Database was empty on first boot")
            if (dbIsEmpty) {
                chapterRepository.loadInitialData(initialChapterList)
            }
        }
        // this variable is null on first access, and hence triggers the reinitialization of the database content
        readAllData = chapterRepository.readAllData
        Log.i(TAG, "readAllData variable is ${readAllData.value}")
        updateSelectedChapter(1) // TODO Change to most recent chapter
        _chapterPageMap = initChapterPageMap()
    }

    fun setCompleted(id: Int) {
        viewModelScope.launch {
            chapterRepository.setCompleted(id = id)
        }
    }

    fun unsetAllCompleted() {
        viewModelScope.launch {
            (1..9).forEach {
                chapterRepository.unsetAllCompleted(it)
            }
        }
    }

    fun getCompletedChapters(): Map<Int, Boolean> {
        val completedChapters = mutableMapOf<Int, Boolean>()
        runBlocking {
            (1..9).forEach {
                val completed: Boolean = chapterRepository.getChapter(it).completed
                // Log.i(TAG, "Chapter $it was completed: $completed")
                completedChapters[it] = completed
            }
        }
        Log.i(TAG, "Completed chapters were $completedChapters")
        return completedChapters
    }

    fun getChapterPages(id: Int): List<List<ChapterElement>> {
        return _chapterPageMap.get(key = id)!!
    }

    fun updateSelectedChapter(id: Int) {
        viewModelScope.launch {
            val chapter: Chapter = async {
                chapterRepository.getChapter(id)
            }.await()
            selectedChapterData.setValue(chapter)
        }
    }

    private fun initChapterPageMap(): Map<Int, List<List<ChapterElement>>> {
        return mapOf(
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
    }
}
