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
import com.goldenraven.padawanwallet.data.tutorial.Tutorial
import com.goldenraven.padawanwallet.data.tutorial.TutorialDatabase
import com.goldenraven.padawanwallet.data.tutorial.TutorialRepository
import kotlinx.coroutines.*

private const val TAG = "TutorialViewModel"

class TutorialViewModel(application: Application) : AndroidViewModel(application) {
    val selectedTutorial: MutableState<Int> = mutableStateOf(1)

    private val readAllData: LiveData<List<Tutorial>>
    private val tutorialRepository: TutorialRepository

    private lateinit var _tutorialData: Tutorial
    val tutorialData: Tutorial
        get() = _tutorialData
    val tutorialLiveData: MutableLiveData<Tutorial> = MutableLiveData<Tutorial>(Tutorial(id = 0, title = "", type = "", difficulty = "", completed = false))

    private val _tutorialPageMap: Map<Int, List<List<TutorialElement>>>

    init {
        val tutorialDao = TutorialDatabase.getInstance(application).tutorialDao()
        tutorialRepository = TutorialRepository(tutorialDao)
        // this variable is null on first access, and hence triggers the reinitialization of the database content
        readAllData = tutorialRepository.readAllData
        Log.i(TAG, "readAllData variable is ${readAllData.value}")
        viewModelScope.launch(Dispatchers.IO) { getTutorialData(id = 1) }// TODO Change to most recent tutorial
        _tutorialPageMap = initTutorialPageMap()
        if (readAllData.value.isNullOrEmpty()) initTutorial() // TODO Check if readAllData is initialized in init (might cause tutorial data to get refreshed on startup)
    }

    fun setCompleted(id: Int, completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { tutorialRepository.setCompleted(id = id, completed = completed) } // TODO Check if calling a coroutine is necessary
        _tutorialData.completed = completed
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

    // suspend fun getTutorialData(id: Int): Tutorial {
    //     val tutorialAsync = viewModelScope.async { tutorialRepository.getTutorial(id = id) }
    //     tutorialAsync.start()
    //     _tutorialData = tutorialAsync.await()
    //     return _tutorialData
    // }

    fun getTutorialData(id: Int): Unit {
        val tutorialData = viewModelScope.async {
            val tutorialAsync = viewModelScope.async { tutorialRepository.getTutorial(id = id) }
            tutorialAsync.start()
            _tutorialData = tutorialAsync.await()
            tutorialLiveData.setValue(_tutorialData)
        }
        Log.i(TAG, "Tutorial data was: $tutorialData")
    }

    // TODO If localization is enabled change language here
    private fun initTutorial() {
        val application: Application = getApplication()
        val tutorialList = listOf(
            Tutorial(id = 1, title = application.getString(R.string.E1_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completed = false),
            Tutorial(id = 2, title = application.getString(R.string.E2_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completed = false),
            Tutorial(id = 3, title = application.getString(R.string.E3_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.basic), completed = false),
            Tutorial(id = 4, title = application.getString(R.string.E4_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completed = false),
            Tutorial(id = 5, title = application.getString(R.string.E5_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.advanced), completed = false),
            Tutorial(id = 6, title = application.getString(R.string.E6_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.advanced), completed = false),
            Tutorial(id = 7, title = application.getString(R.string.E7_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.advanced), completed = false),
            Tutorial(id = 8, title = application.getString(R.string.E8_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.advanced), completed = false),
        )
        viewModelScope.launch(Dispatchers.IO) {
            tutorialRepository.initTutorial(tutorialList = tutorialList)
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
