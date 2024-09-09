/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain.tx

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tx::class], version = 3, exportSchema = false)
abstract class TxDatabase: RoomDatabase() {

    abstract fun txDao(): TxDao

    companion object {
        @Volatile
        private var INSTANCE: TxDatabase? = null

        fun getDatabase(context: Context): TxDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TxDatabase::class.java,
                    "transaction_history",
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
