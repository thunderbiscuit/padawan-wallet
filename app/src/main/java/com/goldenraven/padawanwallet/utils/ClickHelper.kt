package com.goldenraven.padawanwallet.utils

object ClickHelper {
    private val now: Long
        get() = System.currentTimeMillis()
    private var lastEventTimeMs: Long = 0

    fun clickOnce(event: () -> Unit) {
        if (now - lastEventTimeMs >= 800L) {
            event.invoke()
        }
        lastEventTimeMs = now
    }
}
