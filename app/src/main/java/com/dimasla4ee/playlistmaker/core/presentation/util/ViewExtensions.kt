package com.dimasla4ee.playlistmaker.core.presentation.util

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * Sets the visibility of a View.
 *
 * @param show If true, the View is set to `VISIBLE`. Otherwise, it's set to `GONE`.
 */
fun View.show(show: Boolean) {
    this.visibility = if (show) VISIBLE else GONE
}

/**
 * Sets up window insets for a given View.
 *
 * This function applies padding to the `View` to account for specified system UI elements
 * (e.g., system bars, IME).
 * It ensures that the content of the `View` is not obscured by them.
 *
 * The padding is adjusted as follows:
 * - `top`, `left`, `right`: Padded by the insets of the specified `insetsType`.
 * - `bottom`:
 * Padded by the maximum of the specified `insetsType`'s bottom inset and the IME's bottom inset.
 *   This handles cases where the keyboard is visible, ensuring content isn't hidden beneath it.
 *
 * @param insetsType The type of insets to apply padding for (e.g., system bars, navigation bars).
 *                   Defaults to [InsetsTypes.SystemBars].
 *                   This determines which insets are used for
 *                   the top, left, and right padding, and as a baseline for the bottom padding.
 *
 * @param extraInsetsHandler An optional lambda function that will be invoked after the
 *                           window insets have been processed and padding has been applied
 *                           to the `View`.
 *                           It receives the `WindowInsetsCompat` object
 *                           as a parameter, allowing for further custom handling if needed.
 *                           Defaults to `null`.
 */
fun View.setupWindowInsets(
    insetsType: InsetsType = InsetsTypes.SystemBars,
    extraInsetsHandler: ((insets: WindowInsetsCompat) -> Unit)? = null
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val systemBarsInsets = windowInsets.getInsets(insetsType.value)
        val imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())

        view.updatePadding(
            top = systemBarsInsets.top,
            left = systemBarsInsets.left,
            right = systemBarsInsets.right,
            bottom = maxOf(systemBarsInsets.bottom, imeInsets.bottom)
        )

        extraInsetsHandler?.invoke(windowInsets)

        WindowInsetsCompat.CONSUMED
    }
}