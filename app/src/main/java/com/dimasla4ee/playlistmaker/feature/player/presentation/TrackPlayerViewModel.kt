package com.dimasla4ee.playlistmaker.feature.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.utils.LogUtil
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractor
import com.dimasla4ee.playlistmaker.feature.player.presentation.model.PlaylistAddTrackState
import com.dimasla4ee.playlistmaker.feature.playlists.domain.PlaylistInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrackPlayerViewModel(
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val track: Track
) : ViewModel() {

    val isFavorite: StateFlow<Boolean>
        field = MutableStateFlow(false)

    val playlists: StateFlow<List<Playlist>>
        field = MutableStateFlow<List<Playlist>>(emptyList())

    val playlistAddTrackState: SharedFlow<PlaylistAddTrackState>
        field = MutableSharedFlow<PlaylistAddTrackState>()

    fun onViewCreated() {
        viewModelScope.launch {
            isFavorite.update { favoriteInteractor.isFavorite(track.id) }
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

    fun onShowPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect {
                playlists.value = it
            }
        }
    }

    fun onPlaylistClicked(playlist: Playlist) {
        viewModelScope.launch {
            LogUtil.d(
                "TrackPlayerViewModel",
                "onPlaylistClicked pl: ${playlist.id}, tr: ${track.id}"
            )
            val isAdded = playlistInteractor.addTrackToPlaylist(playlist, track)
            val state = if (isAdded) {
                PlaylistAddTrackState.Success(playlist.name)
            } else {
                PlaylistAddTrackState.AlreadyExists(playlist.name)
            }
            playlistAddTrackState.emit(state)
        }
    }

}