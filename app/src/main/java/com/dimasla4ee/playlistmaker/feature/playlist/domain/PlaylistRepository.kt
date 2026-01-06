package com.dimasla4ee.playlistmaker.feature.playlist.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getPlaylistById(id: Int): Playlist
    suspend fun addTrackToPlaylist(playlistId: Int, trackId: Int)
}