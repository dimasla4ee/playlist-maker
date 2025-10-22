package com.dimasla4ee.playlistmaker.core.presentation.util

import androidx.core.view.WindowInsetsCompat

/**
 * A type-safe wrapper class for [WindowInsetsCompat.Type] integer constants.
 */
@JvmInline
value class InsetsType(val value: Int)

/**
 * This object provides access to common inset types from [WindowInsetsCompat.Type],
 * wrapped in the [InsetsType] value class for type safety.
 */
object InsetsTypes {
    val StatusBars = InsetsType(WindowInsetsCompat.Type.statusBars())
    val SystemBars = InsetsType(WindowInsetsCompat.Type.systemBars())
}