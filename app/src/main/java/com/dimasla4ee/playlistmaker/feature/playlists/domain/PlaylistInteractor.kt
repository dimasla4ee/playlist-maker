package com.dimasla4ee.playlistmaker.feature.playlists.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun createPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getPlaylistById(id: Int): Playlist
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int)
    fun getTracksForPlaylist(playlistId: Int): Flow<List<Track>>
}
