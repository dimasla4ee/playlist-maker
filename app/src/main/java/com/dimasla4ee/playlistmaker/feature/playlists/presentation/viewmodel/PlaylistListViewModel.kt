package com.dimasla4ee.playlistmaker.feature.playlists.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.feature.playlists.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.model.PlaylistListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistListViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    val uiState: StateFlow<PlaylistListUiState>
        field = MutableStateFlow<PlaylistListUiState>(PlaylistListUiState.Loading)

    init {
        getPlaylists()
    }

    private fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlists ->
                val newState = if (playlists.isEmpty()) {
                    PlaylistListUiState.Empty
                } else {
                    PlaylistListUiState.Content(playlists)
                }
                uiState.update { newState }
            }
        }
    }

}