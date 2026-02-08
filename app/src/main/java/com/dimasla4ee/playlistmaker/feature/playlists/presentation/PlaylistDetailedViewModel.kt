package com.dimasla4ee.playlistmaker.feature.playlists.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.playlists.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.model.PlaylistDetailedUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistDetailedViewModel(
    private val interactor: PlaylistInteractor,
    private val playlistId: Int
) : ViewModel() {

    val uiState: StateFlow<PlaylistDetailedUiState>
        field = MutableStateFlow<PlaylistDetailedUiState>(PlaylistDetailedUiState.Loading)

    val playlist: StateFlow<Playlist?>
        field = MutableStateFlow<Playlist?>(null)

    val closeScreen: SharedFlow<Unit>
        field = MutableSharedFlow<Unit>()

    fun fetchPlaylistData() {
        viewModelScope.launch {
            val fetchedPlaylist = interactor.getPlaylistById(playlistId)
            playlist.update { fetchedPlaylist }

            interactor.getTracksForPlaylist(playlistId).collect { tracks ->
                val updatedPlaylist = fetchedPlaylist.copy(tracks = tracks)
                playlist.update { updatedPlaylist }

                if (updatedPlaylist.trackCount == 0) {
                    uiState.update { PlaylistDetailedUiState.Empty }
                } else {
                    uiState.update { PlaylistDetailedUiState.Content(tracks) }
                }
            }
        }
    }

    fun onDeletePlaylist() {
        val currentPlaylist = playlist.value ?: return
        viewModelScope.launch {
            interactor.deletePlaylist(currentPlaylist)
            closeScreen.emit(Unit)
        }
    }

    fun onDeleteTrack(track: Track) {
        viewModelScope.launch {
            interactor.deleteTrackFromPlaylist(playlistId, track.id)
            fetchPlaylistData()
        }
    }

}
