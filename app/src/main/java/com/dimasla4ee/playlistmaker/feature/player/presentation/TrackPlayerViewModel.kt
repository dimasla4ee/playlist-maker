package com.dimasla4ee.playlistmaker.feature.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrackPlayerViewModel(
    private val favoriteInteractor: FavoriteInteractor,
    private val track: Track
) : ViewModel() {

    val isFavorite: StateFlow<Boolean>
        field = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            isFavorite.update { favoriteInteractor.getTrackById(track.id) != null }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (isFavorite.value) {
                favoriteInteractor.deleteTrack(track.id)
                isFavorite.update { false }
            } else {
                favoriteInteractor.addTrack(track)
                isFavorite.update { true }
            }
        }
    }

}