package com.dimasla4ee.playlistmaker.app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val switchCheckedThumb: Color,
    val switchCheckedTrack: Color,
    val switchUncheckedThumb: Color,
    val switchUncheckedTrack: Color,
    val settingDrawable: Color
)

val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("No colors provided")
}

val LightAppColors = AppColors(
    switchCheckedThumb = Blue,
    switchCheckedTrack = SoftBlue,
    switchUncheckedThumb = Gray,
    switchUncheckedTrack = LightGray,
    settingDrawable = Gray
)

val DarkAppColors = AppColors(
    switchCheckedThumb = Blue,
    switchCheckedTrack = SoftBlue,
    switchUncheckedThumb = Gray,
    switchUncheckedTrack = LightGray,
    settingDrawable = White
)
