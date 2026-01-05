package com.dimasla4ee.playlistmaker.feature.new_playlist.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
}
