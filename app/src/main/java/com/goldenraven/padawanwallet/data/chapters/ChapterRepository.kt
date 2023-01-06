/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data.chapters

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.runBlocking

private const val TAG = "ChapterRepository"

class ChapterRepository(private val chapterDao: ChapterDao) {
    val readAllData: LiveData<List<Chapter>> = chapterDao.readAllChapters()

    internal suspend fun getChapter(id: Int): Chapter {
        Log.i(TAG, "Querying for chapter $id")
        return chapterDao.getChapter(id = id)
    }

    internal suspend fun setCompleted(id: Int) {
        chapterDao.setCompleted(id = id, completed = true)
    }

    internal suspend fun unsetAllCompleted(id: Int) {
        chapterDao.setCompleted(id = id, completed = false)
    }

    private suspend fun addChapter(chapter: Chapter) {
        chapterDao.addChapter(chapter = chapter)
    }

    suspend fun dbIsEmpty(): Boolean {
        return chapterDao.isEmpty()
    }

    suspend fun loadInitialData(initialChapterList: List<Chapter>) {
        runBlocking {
            initialChapterList.forEach {
                addChapter(chapter = it)
                Log.i(TAG, "Loading initial chapter ${it.id}")
            }
        }
    }
}
