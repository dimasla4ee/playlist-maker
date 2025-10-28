package com.dimasla4ee.playlistmaker.feature.search.presentation.model

import com.dimasla4ee.playlistmaker.core.domain.model.Track

sealed interface SearchScreenState {

    object Loading : SearchScreenState

    data class SearchHistory(
        val searchHistory: List<Track>
    ) : SearchScreenState

    data class Content(
        val results: List<Track> = listOf()
    ) : SearchScreenState

    object Error : SearchScreenState

    object NoResults : SearchScreenState
}