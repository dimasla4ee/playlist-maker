package com.dimasla4ee.playlistmaker.feature.playlists.presentation

sealed interface NavigationEvent {
    data object ShowExitConfirmation : NavigationEvent
    data object PopBackStack : NavigationEvent
}