package com.dimasla4ee.playlistmaker.core.utils

import android.content.res.TypedArray
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.annotation.StyleableRes

fun TypedArray.getAvd(@StyleableRes index: Int): AnimatedVectorDrawable? =
    getDrawable(index)?.mutate() as? AnimatedVectorDrawable