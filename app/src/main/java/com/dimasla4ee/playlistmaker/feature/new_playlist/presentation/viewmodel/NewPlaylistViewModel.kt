package com.dimasla4ee.playlistmaker.feature.new_playlist.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.core.data.local.ImageStorageManager
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.feature.new_playlist.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.new_playlist.presentation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val imageStorageManager: ImageStorageManager
) : ViewModel() {

    val name = MutableStateFlow("")
    val description = MutableStateFlow("")
    val cover = MutableStateFlow(Uri.EMPTY)

    private val _showExitConfirmation = MutableSharedFlow<NavigationEvent>()
    val showExitConfirmation: SharedFlow<NavigationEvent> = _showExitConfirmation

    private val hasChanges: Boolean
        get() = name.value.isNotBlank() || description.value.isNotBlank() || cover.value != Uri.EMPTY

    fun onNameChanged(newName: String) {
        name.update { newName }
    }

    fun onDescriptionChanged(newDescription: String) {
        description.update { newDescription }
    }

    fun onCoverChanged(newCover: Uri) {
        cover.update { newCover }
    }

    fun onBackButtonPressed() {
        viewModelScope.launch {
            val event = if (hasChanges) {
                NavigationEvent.ShowExitConfirmation
            } else {
                NavigationEvent.PopBackStack
            }
            _showExitConfirmation.emit(event)
        }
    }

    fun onCreatePlaylist() {
        viewModelScope.launch {
            val coverPath = if (cover.value != Uri.EMPTY) {
                imageStorageManager.saveImage(cover.value)
            } else {
                ""
            }
            playlistInteractor.createPlaylist(
                Playlist(
                    name = name.value,
                    description = description.value,
                    coverPath = coverPath
                )
            )
        }
    }
}