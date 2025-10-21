package com.dimasla4ee.playlistmaker.presentation.model

sealed interface SearchActivityState {

    object Loading : SearchActivityState

    object SearchHistory : SearchActivityState

    object Content : SearchActivityState

    object Error : SearchActivityState

    object NoResults : SearchActivityState
}