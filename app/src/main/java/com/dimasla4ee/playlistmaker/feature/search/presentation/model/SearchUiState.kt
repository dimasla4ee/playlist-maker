package com.dimasla4ee.playlistmaker.feature.search.presentation.model

import com.dimasla4ee.playlistmaker.core.domain.model.Track

sealed interface SearchUiState {

    data object Idle : SearchUiState
    data class History(val history: List<Track>) : SearchUiState
    data object Loading : SearchUiState
    data class Content(val results: List<Track> = listOf()) : SearchUiState
    data object Error : SearchUiState
    data object NoResults : SearchUiState

}