package com.dimasla4ee.playlistmaker.feature.favorite.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractor
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.model.FavoriteUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(
    favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val favoriteTracks = favoriteInteractor.favoriteTracks()

    val uiState: StateFlow<FavoriteUiState> = favoriteTracks
        .map { tracks ->
            if (tracks.isEmpty()) {
                FavoriteUiState.Empty
            } else {
                FavoriteUiState.Content(tracks)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = FavoriteUiState.Idle
        )

}