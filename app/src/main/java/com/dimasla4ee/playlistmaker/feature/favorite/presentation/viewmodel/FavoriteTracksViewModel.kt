package com.dimasla4ee.playlistmaker.feature.favorite.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractor
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.model.FavoriteTracksUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoriteTracksViewModel(
    favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val favoriteTracks = favoriteInteractor.favoriteTracks()

    val uiState: StateFlow<FavoriteTracksUiState> = favoriteTracks
        .map { tracks ->
            if (tracks.isEmpty()) {
                FavoriteTracksUiState.Empty
            } else {
                FavoriteTracksUiState.Content(tracks)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = FavoriteTracksUiState.Idle
        )

}