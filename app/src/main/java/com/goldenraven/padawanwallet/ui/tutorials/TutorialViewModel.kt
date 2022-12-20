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
import com.goldenraven.padawanwallet.data.tutorial.Tutorial
import com.goldenraven.padawanwallet.data.tutorial.TutorialDatabase
import com.goldenraven.padawanwallet.data.tutorial.TutorialRepository
import kotlinx.coroutines.*

private const val TAG = "TutorialViewModel"

class TutorialViewModel(application: Application) : AndroidViewModel(application) {
    val selectedTutorial: MutableState<Int> = mutableStateOf(1)

    private val readAllData: LiveData<List<Tutorial>>
    private val tutorialRepository: TutorialRepository

    // private lateinit var _tutorialData: Tutorial
    // val tutorialData: Tutorial
    //     get() = _tutorialData
    val selectedTutorialData: MutableLiveData<Tutorial> = MutableLiveData<Tutorial>(Tutorial(id = 0, title = "", type = "", difficulty = "", completed = false))

    private val _tutorialPageMap: Map<Int, List<List<TutorialElement>>>

    init {
        val tutorialDao = TutorialDatabase.getInstance(application).tutorialDao()
        tutorialRepository = TutorialRepository(tutorialDao)
        // this variable is null on first access, and hence triggers the reinitialization of the database content
        readAllData = tutorialRepository.readAllData
        Log.i(TAG, "readAllData variable is ${readAllData.value}")
        // viewModelScope.launch(Dispatchers.IO) { getTutorialData(id = 1) }//
        updateSelectedTutorial(1) // TODO Change to most recent tutorial
        _tutorialPageMap = initTutorialPageMap()
        // if (readAllData.value.isNullOrEmpty()) initTutorial() // TODO Check if readAllData is initialized in init (might cause tutorial data to get refreshed on startup)
    }

    fun setCompleted(id: Int, completed: Boolean) {
        viewModelScope.launch {
            tutorialRepository.setCompleted(id = id, completed = completed)
        }
    }

    fun getCompletedTutorials(): Map<Int, Boolean> {
        val completedTutorials = mutableMapOf<Int, Boolean>()
        runBlocking {
            (1..8).forEach {
                val completed: Boolean = tutorialRepository.getTutorial(it).completed
                Log.i(TAG, "Tutorial $it was completed: $completed")
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
        )
    }
}
