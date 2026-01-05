package com.dimasla4ee.playlistmaker.feature.new_playlist.presentation.viewmodel

sealed interface NavigationEvent {
    data object ShowExitConfirmation : NavigationEvent
    data object PopBackStack : NavigationEvent
}