/*
 * Copyright 2020-2022 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package com.goldenraven.padawanwallet.data

import androidx.lifecycle.LiveData

class TxRepository(private val txDao: TxDao) {
    val readAllData: LiveData<List<Tx>> = txDao.readAllTx()

    fun addTx(tx: Tx) {
        txDao.addTx(tx)
    }
}