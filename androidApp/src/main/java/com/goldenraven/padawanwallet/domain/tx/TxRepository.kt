/*
 * Copyright 2020-2023 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.domain.tx

import kotlinx.coroutines.flow.Flow

class TxRepository(private val txDao: TxDao) {
    val readAllData: Flow<List<Tx>> = txDao.readAllTx()

    fun addTx(tx: Tx) {
        txDao.addTx(tx = tx)
    }
}
