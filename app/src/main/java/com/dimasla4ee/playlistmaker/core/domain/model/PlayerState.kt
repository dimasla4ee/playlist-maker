package com.dimasla4ee.playlistmaker.core.domain.model

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val progress: String) {

    object Default : PlayerState(isPlayButtonEnabled = false, progress = "00:00")
    object Prepared : PlayerState(isPlayButtonEnabled = true, progress = "00:00")
    class Playing(progress: String) : PlayerState(isPlayButtonEnabled = true, progress)
    class Paused(progress: String) : PlayerState(isPlayButtonEnabled = true, progress)

}