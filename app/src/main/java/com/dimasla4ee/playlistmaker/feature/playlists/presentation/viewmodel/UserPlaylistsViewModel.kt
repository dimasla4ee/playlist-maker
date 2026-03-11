package com.dimasla4ee.playlistmaker.feature.playlists.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.feature.playlists.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.model.UserPlaylistsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserPlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    val uiState: StateFlow<UserPlaylistsUiState>
        field = MutableStateFlow<UserPlaylistsUiState>(UserPlaylistsUiState.Loading)

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlists ->
                val newState = if (playlists.isEmpty()) {
                    UserPlaylistsUiState.Empty
                } else {
                    UserPlaylistsUiState.Content(playlists)
                }
                uiState.update { newState }
            }
        }
    }

}