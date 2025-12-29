package com.dimasla4ee.playlistmaker.feature.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TrackPlayerViewModel(
    private val favoriteInteractor: FavoriteInteractor,
    private val track: Track
) : ViewModel() {

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: Flow<Boolean> get() = _isFavorite

    init {
        viewModelScope.launch {
            _isFavorite.value = favoriteInteractor.getTrackById(track.id) != null
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (_isFavorite.value) {
                favoriteInteractor.deleteTrack(track.id)
            } else {
                favoriteInteractor.addTrack(track)
            }
        }
        _isFavorite.value = !_isFavorite.value
    }

}