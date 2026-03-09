package com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.core.domain.model.PlayerController
import com.dimasla4ee.playlistmaker.core.domain.model.PlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MediaPlayerViewModel : ViewModel() {

    val state: StateFlow<PlayerState>
        field = MutableStateFlow<PlayerState>(PlayerState.Default)

    private var controller: PlayerController? = null

    fun setPlayerController(controller: PlayerController) {
        this.controller = controller

        viewModelScope.launch {
            controller.getPlayerState().collect { playerState ->
                state.update { playerState }
            }
        }
    }

    fun removePlayerController() {
        controller = null
    }

    fun onPlayButtonClicked() = when (state.value) {
        is PlayerState.Playing -> controller?.pause()

        is PlayerState.Prepared,
        is PlayerState.Paused -> controller?.play()

        else -> Unit
    }

    fun onResume() = controller?.cancelNotification()

    fun onPause() {
        if (state.value is PlayerState.Playing) {
            controller?.startNotification()
        }
    }

}
