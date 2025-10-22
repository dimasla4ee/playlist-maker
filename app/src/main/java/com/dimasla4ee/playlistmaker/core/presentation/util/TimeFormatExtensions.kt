package com.dimasla4ee.playlistmaker.core.presentation.util

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * A shared [SimpleDateFormat] instance for formatting time values into a "mm:ss" pattern.
 * [Locale.ROOT] ensures consistent formatting regardless of the user's device locale.
 */
private val dateFormatter = SimpleDateFormat("mm:ss", Locale.ROOT)

/**
 * Converts an [Int] representing milliseconds into a "mm:ss" formatted string.
 * @return A [String] in the "mm:ss" format.
 */
fun Int.toMmSs(): String = dateFormatter.format(this)

/**
 * Converts a [Long] representing milliseconds into a "mm:ss" formatted string.
 * @return A [String] in the "mm:ss" format.
 */
fun Long.toMmSs(): String = dateFormatter.format(this)