package com.goldenraven.padawanwallet.utils

class ClickHelper private constructor() {
    private val now: Long
        get() = System.currentTimeMillis()
    private var lastEventTimeMs: Long = 0
    fun clickOnce(event: () -> Unit) {
        if (now - lastEventTimeMs >= 800L) {
            event.invoke()
        }
        lastEventTimeMs = now
    }
    companion object {
        @Volatile
        private var instance: ClickHelper? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ClickHelper().also { instance = it }
            }
    }
}
