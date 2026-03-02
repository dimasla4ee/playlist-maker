package com.dimasla4ee.playlistmaker.core.domain.model

import kotlinx.coroutines.flow.StateFlow

interface PlayerController {
    fun getPlayerState(): StateFlow<PlayerState>
    fun play()
    fun pause()

    fun startNotification()
    fun cancelNotification()
}