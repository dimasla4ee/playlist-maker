package com.dimasla4ee.playlistmaker.feature.favorite.presentation.model

import com.dimasla4ee.playlistmaker.core.domain.model.Track

sealed interface FavoriteUiState {

    data object Empty : FavoriteUiState
    data class Content(val tracks: List<Track>) : FavoriteUiState

}