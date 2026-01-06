package com.dimasla4ee.playlistmaker.feature.player.presentation.model

sealed class PlaylistAddTrackState {
    data class Success(val playlistName: String) : PlaylistAddTrackState()
    data class AlreadyExists(val playlistName: String) : PlaylistAddTrackState()
}