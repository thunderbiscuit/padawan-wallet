/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.ui.tutorials

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.goldenraven.padawanwallet.R

class TutorialViewModel(application: Application) : AndroidViewModel(application) {
    private val _tutorialsList: List<TutorialData>
    val tutorialsList: List<TutorialData>
        get() = _tutorialsList

    init {
        _tutorialsList = listOf(
            TutorialData(id = 1, title = application.getString(R.string.E1_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completion = 0, data = tutorial1),
            TutorialData(id = 2, title = application.getString(R.string.E2_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completion = 0, data = tutorial2),
            TutorialData(id = 3, title = application.getString(R.string.E3_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.basic), completion = 0, data = tutorial3),
            TutorialData(id = 4, title = application.getString(R.string.E4_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.basic), completion = 0, data = tutorial4),
            TutorialData(id = 5, title = application.getString(R.string.E5_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.advanced), completion = 0, data = tutorial5),
            TutorialData(id = 6, title = application.getString(R.string.E6_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.advanced), completion = 0, data = tutorial6),
            TutorialData(id = 7, title = application.getString(R.string.E7_title), type = application.getString(R.string.concept), difficulty = application.getString(R.string.advanced), completion = 0, data = tutorial7),
            TutorialData(id = 8, title = application.getString(R.string.E8_title), type = application.getString(R.string.skill), difficulty = application.getString(R.string.advanced), completion = 0, data = tutorial8),
        )
    }

    fun getTutorial(id: Int): TutorialData {
        return _tutorialsList.first { it.id == id }
    }
}