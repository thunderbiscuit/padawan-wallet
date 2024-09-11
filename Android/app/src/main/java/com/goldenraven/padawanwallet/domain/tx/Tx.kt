/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain.tx

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_history")
data class Tx(
    @PrimaryKey(autoGenerate = false)
    val txid: String,
    val date: String,
    val valueIn: Long,
    val valueOut: Long,
    val fee: Long,
    val isPayment: Boolean,
    val height: Int,
)
