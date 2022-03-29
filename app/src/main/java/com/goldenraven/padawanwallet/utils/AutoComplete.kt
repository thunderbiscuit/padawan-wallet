package com.goldenraven.padawanwallet.utils

typealias CustomFiler<T> = (T, String) -> Boolean

fun <T> List<T>.isAutoCompleteEntities(filter: CustomFiler<T>): List<MnemonicAutoCompleteEntity<T>> {
    return map {
        object : MnemonicAutoCompleteEntity<T> {
            override val value: T
                get() = it

            override fun filter(query: String): Boolean {
                return filter(value, query)
            }
        }
    }
}