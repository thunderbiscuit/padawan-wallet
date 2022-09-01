/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data.tutorial

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tutorial::class], version = 1, exportSchema = false)
abstract class TutorialDatabase: RoomDatabase() {
    abstract fun tutorialDao(): TutorialDao

    companion object {
        @Volatile
        private var INSTANCE: TutorialDatabase? = null

        fun getInstance(context: Context): TutorialDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TutorialDatabase::class.java,
                    "tutorial_db",
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
