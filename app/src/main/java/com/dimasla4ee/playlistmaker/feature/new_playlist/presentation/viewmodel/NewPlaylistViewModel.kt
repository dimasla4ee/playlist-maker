package com.dimasla4ee.playlistmaker.feature.new_playlist.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class NewPlaylistViewModel(
    //TODO: Interactor
) : ViewModel() {

    val name: StateFlow<String>
        field = MutableStateFlow("")

    val description: StateFlow<String>
        field = MutableStateFlow("")

    val cover: StateFlow<Uri>
        field = MutableStateFlow(Uri.EMPTY)

    val showExitConfirmation: SharedFlow<NavigationEvent>
        field = MutableSharedFlow<NavigationEvent>()

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
            showExitConfirmation.emit(event)
        }
    }

    fun onCreatePlaylist() {
        // TODO: create playlist
    }

}

