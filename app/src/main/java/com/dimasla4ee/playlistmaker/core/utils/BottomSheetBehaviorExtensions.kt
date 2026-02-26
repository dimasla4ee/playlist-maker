package com.dimasla4ee.playlistmaker.core.utils

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun <T : View> BottomSheetBehavior<T>.hide() {
    state = BottomSheetBehavior.STATE_HIDDEN
}

fun <T : View> BottomSheetBehavior<T>.collapse() {
    state = BottomSheetBehavior.STATE_COLLAPSED
}