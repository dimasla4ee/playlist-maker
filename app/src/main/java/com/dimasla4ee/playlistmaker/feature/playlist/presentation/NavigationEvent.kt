package com.dimasla4ee.playlistmaker.feature.playlist.presentation

sealed interface NavigationEvent {
    data object ShowExitConfirmation : NavigationEvent
    data object PopBackStack : NavigationEvent
}