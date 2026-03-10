package com.dimasla4ee.playlistmaker.app.ui.theme

import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable

@Composable
fun appSwitchColors() = SwitchDefaults.colors(
    checkedThumbColor = LocalAppColors.current.switchCheckedThumb,
    checkedTrackColor = LocalAppColors.current.switchCheckedTrack,
    uncheckedThumbColor = LocalAppColors.current.switchUncheckedThumb,
    uncheckedTrackColor = LocalAppColors.current.switchUncheckedTrack
)
