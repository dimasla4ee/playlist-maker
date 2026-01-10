package com.dimasla4ee.playlistmaker.feature.playlists.presentation.viewmodel

import android.net.Uri
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
    //TODO: add Playlist parameter to constructor
) : ViewModel() {

    val uiState: StateFlow<NewPlaylistUiState>
        field = MutableStateFlow(NewPlaylistUiState())

    val showExitConfirmation: SharedFlow<NavigationEvent>
        field = MutableSharedFlow<NavigationEvent>()

    //TODO: depend on Playlist parameter
    private val hasChanges: Boolean
        get() = with(uiState.value) {
            !nameIsBlank() || !descriptionIsBlank() || !coverIsEmpty()
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
            val currentState = uiState.value
            val coverPath = if (!currentState.coverIsEmpty()) {
                imageStorageManager.saveImage(currentState.coverUri)
            } else {
                ""
            }
            playlistInteractor.createPlaylist(
                Playlist(
                    name = currentState.name,
                    description = currentState.description,
                    coverPath = coverPath
                )
            )
        }
    }

    data class NewPlaylistUiState(
        val name: String = "",
        val description: String = "",
        val coverUri: Uri = Uri.EMPTY
    ) {
        fun nameIsBlank() = name.isBlank()
        fun descriptionIsBlank() = description.isBlank()
        fun coverIsEmpty() = coverUri == Uri.EMPTY
    }

}