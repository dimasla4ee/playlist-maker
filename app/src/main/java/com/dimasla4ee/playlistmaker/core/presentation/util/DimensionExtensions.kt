package com.dimasla4ee.playlistmaker.core.presentation.util

import android.content.Context
import android.util.TypedValue

/**
 * Converts a value in density-independent pixels (dp) to pixels (px).
 *
 * @param context The context to access resources and display metrics.
 * @return The converted value in pixels.
 */
fun Float.dpToPx(context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this,
    context.resources.displayMetrics
)