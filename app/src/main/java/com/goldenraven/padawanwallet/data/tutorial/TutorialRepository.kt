/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data.tutorial

import androidx.lifecycle.LiveData

class TutorialRepository(private val tutorialDao: TutorialDao) {
    val readAllData: LiveData<List<Tutorial>> = tutorialDao.readAllTutorial()

    internal suspend fun getTutorial(id: Int): Tutorial {
        return tutorialDao.getTutorial(id = id)
    }

    internal suspend fun setCompletion(id: Int, completion: Int) {
        tutorialDao.setCompletion(id = id, completion = completion)
    }

    private suspend fun addTutorial(tutorial: Tutorial) {
        tutorialDao.addTutorial(tutorial = tutorial)
    }

    internal suspend fun initTutorial(tutorialList: List<Tutorial>) {
        tutorialList.forEach {
            addTutorial(tutorial = it)
        }
    }
}