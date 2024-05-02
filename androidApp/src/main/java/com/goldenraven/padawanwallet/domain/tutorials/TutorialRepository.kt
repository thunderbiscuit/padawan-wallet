/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain.tutorials

import android.content.SharedPreferences

const val defaultCompletedTutorials = "false,false,false,false,false,false,false,false,false"

object TutorialRepository {
    private lateinit var sharedPreferences: SharedPreferences
    fun setSharedPreferences(sharedPref: SharedPreferences) {
        sharedPreferences = sharedPref
    }

    fun getCompletedTutorials(): Map<Int, Boolean> {
        val completedTutorialsString: String = sharedPreferences.getString("completedTutorials", defaultCompletedTutorials) ?: defaultCompletedTutorials
        val list = completedTutorialsString.split(",").map { it.toBooleanStrict() }
        return list.mapIndexed { index, value -> index + 1 to value }.toMap()
    }

    fun setCompletedTutorial(index: Int) {
        val completedTutorials: MutableMap<Int, Boolean> = getCompletedTutorials().toMutableMap()
        completedTutorials[index] = true
        val completedTutorialsString = completedTutorials.flatMap { listOf(it.value) }.joinToString(separator = ",")
        sharedPreferences.edit().apply {
            putString("completedTutorials", completedTutorialsString)
        }.apply()
    }

    fun unsetAllCompleted() {
        sharedPreferences.edit().apply {
            putString("completedTutorials", defaultCompletedTutorials)
        }.apply()
    }
}

