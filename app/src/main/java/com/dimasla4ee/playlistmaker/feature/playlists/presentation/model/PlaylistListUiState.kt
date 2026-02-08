package com.dimasla4ee.playlistmaker.feature.playlists.presentation.model

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist

sealed interface PlaylistListUiState {
    object Loading : PlaylistListUiState
    object Empty : PlaylistListUiState
    data class Content(val playlists: List<Playlist>) : PlaylistListUiState
}
