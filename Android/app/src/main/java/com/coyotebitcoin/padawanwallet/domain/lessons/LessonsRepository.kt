/*
 * Copyright 2020-2025 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.coyotebitcoin.padawanwallet.domain.lessons

import android.content.SharedPreferences

const val defaultCompletedLessons = "false,false,false,false,false,false,false,false,false"

object LessonsRepository {
    private lateinit var sharedPreferences: SharedPreferences
    fun setSharedPreferences(sharedPref: SharedPreferences) {
        sharedPreferences = sharedPref
    }

    fun getCompletedLessons(): Map<Int, Boolean> {
        val completedLessonsString: String = sharedPreferences.getString("completedLessons", defaultCompletedLessons) ?: defaultCompletedLessons
        val list = completedLessonsString.split(",").map { it.toBooleanStrict() }
        return list.mapIndexed { index, value -> index + 1 to value }.toMap()
    }

    fun setCompletedLesson(index: Int) {
        val completedLessons: MutableMap<Int, Boolean> = getCompletedLessons().toMutableMap()
        completedLessons[index] = true
        val completedLessonsString = completedLessons.flatMap { listOf(it.value) }.joinToString(separator = ",")
        sharedPreferences.edit().apply {
            putString("completedLessons", completedLessonsString)
        }.apply()
    }

    fun resetCompletedLessons() {
        sharedPreferences.edit().apply {
            putString("completedLessons", defaultCompletedLessons)
        }.apply()
    }
}
