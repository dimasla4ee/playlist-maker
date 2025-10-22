package com.dimasla4ee.playlistmaker.core.util

import android.util.Log
import com.dimasla4ee.playlistmaker.BuildConfig

/**
 * Utility object for logging messages.
 *
 * This object is a wrapper for Log API that provides methods to log messages
 * that will only be output when the application is in debug mode(i.e., `BuildConfig.DEBUG` is true).
 */
@Suppress("unused")
object LogUtil {
    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }
}