package com.dimasla4ee.playlistmaker.feature.media_library.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.model.PlaylistsState
import com.dimasla4ee.playlistmaker.feature.new_playlist.domain.PlaylistInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val _state = MutableStateFlow<PlaylistsState>(PlaylistsState.Loading)
    val state = _state.asStateFlow()

    init {
        getPlaylists()
    }

    private fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlists ->
                if (playlists.isEmpty()) {
                    _state.value = PlaylistsState.Empty
                } else {
                    _state.value = PlaylistsState.Content(playlists)
                }
            }
        }
    }
}