/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTx(tx: Tx)

    @Query("SELECT * FROM transaction_history ORDER BY date DESC")
    fun readAllTx(): LiveData<List<Tx>>
}
