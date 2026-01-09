package com.dimasla4ee.playlistmaker.feature.playlist.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun createPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlistId: Int, trackId: Int): Boolean
}