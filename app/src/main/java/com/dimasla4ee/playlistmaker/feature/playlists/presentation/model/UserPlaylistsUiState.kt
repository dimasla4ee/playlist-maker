package com.dimasla4ee.playlistmaker.feature.playlists.presentation.model

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist

sealed interface UserPlaylistsUiState {
    object Loading : UserPlaylistsUiState
    object Empty : UserPlaylistsUiState
    data class Content(val playlists: List<Playlist>) : UserPlaylistsUiState
}
