package com.dimasla4ee.playlistmaker.feature.search.presentation.model

import com.dimasla4ee.playlistmaker.core.domain.model.Track

sealed interface SearchActivityState {

    object Loading : SearchActivityState

    data class SearchHistory(
        val searchHistory: List<Track>
    ) : SearchActivityState

    data class Content(
        val results: List<Track> = listOf()
    ) : SearchActivityState

    object Error : SearchActivityState

    object NoResults : SearchActivityState
}