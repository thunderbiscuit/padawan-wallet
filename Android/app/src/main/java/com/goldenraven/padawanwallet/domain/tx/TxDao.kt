/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain.tx

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TxDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTx(tx: Tx)

    @Query("SELECT * FROM transaction_history ORDER BY date DESC")
    fun readAllTx(): Flow<List<Tx>>
}
