package com.dimasla4ee.playlistmaker.feature.playlist.presentation.model

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist

sealed interface PlaylistsState {
    object Loading : PlaylistsState
    object Empty : PlaylistsState
    data class Content(val playlists: List<Playlist>) : PlaylistsState
}