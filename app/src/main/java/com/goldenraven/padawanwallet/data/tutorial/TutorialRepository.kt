/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data.tutorial

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.runBlocking

private const val TAG = "TutorialRepository"

class TutorialRepository(private val tutorialDao: TutorialDao) {
    val readAllData: LiveData<List<Tutorial>> = tutorialDao.readAllTutorial()

    val readAllInitialData: List<Tutorial>? = tutorialDao.readAllTutorial().value

    internal suspend fun getTutorial(id: Int): Tutorial {
        return tutorialDao.getTutorial(id = id)
    }

    internal suspend fun setCompleted(id: Int, completed: Boolean) {
        tutorialDao.setCompleted(id = id, completed = completed)
    }

    private suspend fun addTutorial(tutorial: Tutorial) {
        tutorialDao.addTutorial(tutorial = tutorial)
    }

    suspend fun initTutorial(tutorialList: List<Tutorial>) {
        tutorialList.forEach {
            addTutorial(tutorial = it)
        }
    }

    suspend fun dbIsEmpty(): Boolean {
        return tutorialDao.isEmpty()
    }

    suspend fun loadInitialData(initialChapterList: List<Tutorial>) {
        runBlocking {
            initialChapterList.forEach {
                addTutorial(tutorial = it)
                Log.i(TAG, "Loading initial chapter ${it.id}")
            }
        }
    }
}
