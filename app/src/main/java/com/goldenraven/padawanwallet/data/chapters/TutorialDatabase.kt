/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data.chapters

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tutorial::class], version = 3, exportSchema = false)
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
                val instance = Room.databaseBuilder(context.applicationContext, TutorialDatabase::class.java, "tutorial_db")
                    // .createFromAsset("databases/tutorial_db.db")
                    // TODO #1: Having this on makes the pre-populating of the database to re-create it on every boot
                    // because somehow it thinks the db schema has changed?
                    // the createFromAsset only gets called if the db doesn't exist, so this could cause problems if we ever ship a new pre-populated db
                    // where we'd need to delete the database and recreate it from scratch, i.e. what fallback... would do
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
