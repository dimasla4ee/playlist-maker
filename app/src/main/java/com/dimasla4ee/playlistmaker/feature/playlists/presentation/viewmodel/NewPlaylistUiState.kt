package com.dimasla4ee.playlistmaker.feature.playlists.presentation.viewmodel

import android.net.Uri

data class NewPlaylistUiState(
    val name: String = "",
    val description: String = "",
    val coverUri: Uri = Uri.EMPTY
) {
    fun nameIsBlank() = name.isBlank()
    fun descriptionIsBlank() = description.isBlank()
    fun coverIsEmpty() = coverUri == Uri.EMPTY
}