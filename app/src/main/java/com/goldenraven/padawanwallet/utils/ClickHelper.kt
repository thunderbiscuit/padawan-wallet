package com.goldenraven.padawanwallet.utils

/**
 * Helper class to prevent multiple clicks on buttons, opening multiple identical fragments on top of each other.
 * The class will let an event through if the last event was more than 800ms ago.
 */
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
