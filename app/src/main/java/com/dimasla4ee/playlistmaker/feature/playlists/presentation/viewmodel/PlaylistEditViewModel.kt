package com.dimasla4ee.playlistmaker.feature.playlists.presentation.viewmodel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.core.data.storage.ImageStorageManager
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.feature.playlists.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val imageStorageManager: ImageStorageManager
) : ViewModel() {

    val uiState: StateFlow<NewPlaylistUiState>
        field = MutableStateFlow(NewPlaylistUiState())

    val showExitConfirmation: SharedFlow<NavigationEvent>
        field = MutableSharedFlow<NavigationEvent>()

    val initialData: SharedFlow<Playlist>
        field = MutableSharedFlow<Playlist>(replay = 1)

    private var initialPlaylist: Playlist? = null

    fun fetchPlaylistData(id: Int) {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(id)
            initialPlaylist = playlist
            initialData.emit(playlist)
            uiState.update { currentState ->
                currentState.copy(
                    name = playlist.name,
                    description = playlist.description ?: "",
                    coverUri = playlist.coverPath.toUri()
                )
            }
        }
    }

    private val hasChanges: Boolean
        get() = if (initialPlaylist != null) {
            with(uiState.value) {
                name != initialPlaylist?.name ||
                        description != (initialPlaylist?.description ?: "") ||
                        coverUri != initialPlaylist?.coverPath?.toUri()
            }
        } else {
            with(uiState.value) {
                !nameIsBlank() || !descriptionIsBlank() || !coverIsEmpty()
            }
        }

    fun onNameChanged(newName: String) = uiState.update { currentState ->
        currentState.copy(name = newName)
    }

    fun onDescriptionChanged(newDescription: String) = uiState.update { currentState ->
        currentState.copy(description = newDescription)
    }

    fun onCoverChanged(newCoverUri: Uri) = uiState.update { currentState ->
        currentState.copy(coverUri = newCoverUri)
    }

    fun onBackButtonPressed() {
        viewModelScope.launch {
            val event = if (hasChanges) {
                NavigationEvent.ShowExitConfirmation
            } else {
                NavigationEvent.PopBackStack
            }
            showExitConfirmation.emit(event)
        }
    }

    fun onCreatePlaylist() {
        viewModelScope.launch {
            val currentState = uiState.value.copy(
                description = uiState.value.description.trim(),
            )
            
            val coverPath = if (initialPlaylist != null && currentState.coverUri == initialPlaylist?.coverPath?.toUri()) {
                initialPlaylist?.coverPath ?: ""
            } else if (!currentState.coverIsEmpty()) {
                imageStorageManager.saveImage(currentState.coverUri)
            } else {
                ""
            }

            if (initialPlaylist != null) {
                playlistInteractor.updatePlaylist(
                    initialPlaylist!!.copy(
                        name = currentState.name,
                        description = currentState.description,
                        coverPath = coverPath
                    )
                )
            } else {
                playlistInteractor.createPlaylist(
                    Playlist(
                        name = currentState.name,
                        description = currentState.description,
                        coverPath = coverPath
                    )
                )
            }
        }
    }

}

