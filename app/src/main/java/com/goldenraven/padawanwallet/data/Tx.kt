/*
 * Copyright 2020 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_history")
data class Tx(
    @PrimaryKey(autoGenerate = false)
    val txid: String,
    val date: String,
    val valueIn: Int,
    val valueOut: Int,
    val fees: Int,
    val isSend: Boolean,
    val height: Int,
)
