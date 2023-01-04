/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.tutorials

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.goldenraven.padawanwallet.R
import com.goldenraven.padawanwallet.data.chapters.Tutorial
import com.goldenraven.padawanwallet.data.chapters.TutorialDatabase
import com.goldenraven.padawanwallet.data.chapters.TutorialRepository
import kotlinx.coroutines.*

private const val TAG = "TutorialViewModel"

class TutorialViewModel(application: Application) : AndroidViewModel(application) {
    val selectedTutorial: MutableState<Int> = mutableStateOf(1)

    private val readAllData: LiveData<List<Tutorial>>
    private val tutorialRepository: TutorialRepository

    val selectedTutorialData: MutableLiveData<Tutorial> = MutableLiveData<Tutorial>(Tutorial(id = 0, title = "", type = "", difficulty = "", completed = false))

    private val _tutorialPageMap: Map<Int, List<List<TutorialElement>>>

    private val initialChapterList = listOf(
        Tutorial(id = 1, title = application.getString(R.string.C1_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completed = false),
        Tutorial(id = 2, title = application.getString(R.string.C2_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completed = false),
        Tutorial(id = 3, title = application.getString(R.string.C3_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.basic), completed = false),
        Tutorial(id = 4, title = application.getString(R.string.C4_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completed = false),
        Tutorial(id = 5, title = application.getString(R.string.C5_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.advanced), completed = false),
        Tutorial(id = 6, title = application.getString(R.string.C6_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.advanced), completed = false),
        Tutorial(id = 7, title = application.getString(R.string.C7_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.advanced), completed = false),
        Tutorial(id = 8, title = application.getString(R.string.C8_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.advanced), completed = false),
        Tutorial(id = 9, title = application.getString(R.string.C9_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.advanced), completed = false),
    )

    init {
        val tutorialDao = TutorialDatabase.getInstance(application).tutorialDao()
        tutorialRepository = TutorialRepository(tutorialDao)
        runBlocking(Dispatchers.IO) {
            // TODO #2: this is a hack because of issue outlined in TODO #1
            val dbIsEmpty = tutorialRepository.dbIsEmpty()
            Log.i(TAG, "Database was empty on first boot")
            if (dbIsEmpty) {
                tutorialRepository.loadInitialData(initialChapterList)
            }
        }
        // this variable is null on first access, and hence triggers the reinitialization of the database content
        readAllData = tutorialRepository.readAllData
        Log.i(TAG, "readAllData variable is ${readAllData.value}")
        updateSelectedTutorial(1) // TODO Change to most recent tutorial
        _tutorialPageMap = initTutorialPageMap()
    }

    fun setCompleted(id: Int) {
        viewModelScope.launch {
            tutorialRepository.setCompleted(id = id)
        }
    }

    fun unsetAllCompleted(): Unit {
        viewModelScope.launch {
            (1..9).forEach {
                tutorialRepository.unsetAllCompleted(it)
            }
        }
    }

    fun getCompletedTutorials(): Map<Int, Boolean> {
        val completedTutorials = mutableMapOf<Int, Boolean>()
        runBlocking {
            (1..9).forEach {
                val completed: Boolean = tutorialRepository.getTutorial(it).completed
                // Log.i(TAG, "Tutorial $it was completed: $completed")
                completedTutorials[it] = completed
            }
        }
        Log.i(TAG, "Completed tutorials were $completedTutorials")
        return completedTutorials
    }

    fun getTutorialPages(id: Int): List<List<TutorialElement>> {
        return _tutorialPageMap.get(key = id)!!
    }

    fun updateSelectedTutorial(id: Int): Unit {
        viewModelScope.launch {
            val tutorial: Tutorial = async {
                tutorialRepository.getTutorial(id)
            }.await()
            selectedTutorialData.setValue(tutorial)
        }
    }

    private fun initTutorialPageMap(): Map<Int, List<List<TutorialElement>>> {
        return mapOf(
            Pair(1, tutorial1),
            Pair(2, tutorial2),
            Pair(3, tutorial3),
            Pair(4, tutorial4),
            Pair(5, tutorial5),
            Pair(6, tutorial6),
            Pair(7, tutorial7),
            Pair(8, tutorial8),
            Pair(9, tutorial9),
        )
    }
}
