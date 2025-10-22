package com.dimasla4ee.playlistmaker.core.presentation.util

import android.os.Handler
import android.os.Looper

object Debouncer {
    private const val CLICK_DEBOUNCE_MILLIS = 1000L
    private const val DEFAULT_DEBOUNCE_MILLIS = 2000L

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    fun debounce(
        delayMillis: Long = DEFAULT_DEBOUNCE_MILLIS,
        action: Runnable
    ) {
        handler.removeCallbacks(action)
        handler.postDelayed(action, delayMillis)
    }

    fun cancel(action: Runnable) = handler.removeCallbacks(action)

    fun isClickAllowed(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            debounce(CLICK_DEBOUNCE_MILLIS) { isClickAllowed = true }
        }
        return current
    }
}