package com.goldenraven.padawanwallet.utils

import androidx.compose.runtime.Stable

@Stable
interface MnemonicAutoCompleteEntity<T> : AutoCompleteEntity {
    val value: T
}

@Stable
interface AutoCompleteEntity {
    fun filter(query: String): Boolean
}