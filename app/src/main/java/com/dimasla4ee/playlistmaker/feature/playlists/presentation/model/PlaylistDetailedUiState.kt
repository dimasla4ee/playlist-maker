package com.dimasla4ee.playlistmaker.feature.playlists.presentation.model

import com.dimasla4ee.playlistmaker.core.domain.model.Track

sealed interface PlaylistDetailedUiState {
    object Loading : PlaylistDetailedUiState
    object Empty : PlaylistDetailedUiState
    data class Content(val tracks: List<Track>) : PlaylistDetailedUiState
}
