package com.dimasla4ee.playlistmaker.feature.playlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.feature.playlist.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.playlist.presentation.model.PlaylistsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    val state: StateFlow<PlaylistsState>
        field = MutableStateFlow<PlaylistsState>(PlaylistsState.Loading)

    init {
        getPlaylists()
    }

    private fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlists ->
                val newState = if (playlists.isEmpty()) {
                    PlaylistsState.Empty
                } else {
                    PlaylistsState.Content(playlists)
                }
                state.update { newState }
            }
        }
    }
}