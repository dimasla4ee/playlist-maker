package com.dimasla4ee.playlistmaker.core.presentation.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.DrawableCompat

fun Context.tintedDrawable(
    @DrawableRes drawableResId: Int,
    @ColorRes colorResId: Int? = null
): Drawable? {
    val drawable = AppCompatResources.getDrawable(this, drawableResId) ?: return null

    if (colorResId != null) {
        val color = getColor(this, colorResId)
        DrawableCompat.wrap(drawable).mutate()
        DrawableCompat.setTint(drawable, color)
    }

    return drawable
}