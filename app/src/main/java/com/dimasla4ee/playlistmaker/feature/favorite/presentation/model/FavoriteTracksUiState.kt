package com.dimasla4ee.playlistmaker.feature.favorite.presentation.model

import com.dimasla4ee.playlistmaker.core.domain.model.Track

sealed interface FavoriteTracksUiState {

    data object Idle : FavoriteTracksUiState
    data object Empty : FavoriteTracksUiState
    data class Content(val tracks: List<Track>) : FavoriteTracksUiState

}