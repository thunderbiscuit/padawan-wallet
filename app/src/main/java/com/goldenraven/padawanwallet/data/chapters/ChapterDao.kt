/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data.chapters

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChapter(chapter: Chapter)

    @Query("SELECT * FROM chapters_db ORDER BY id ASC")
    fun readAllChapters(): Flow<List<Chapter>>

    @Query("UPDATE chapters_db SET completed = :completed WHERE id = :id")
    suspend fun setCompleted(id: Int, completed: Boolean)

    @Query("SELECT * FROM chapters_db WHERE id = :id")
    suspend fun getChapter(id: Int): Chapter

    @Query("SELECT (SELECT COUNT(*) FROM chapters_db) == 0")
    fun isEmpty(): Boolean
}
