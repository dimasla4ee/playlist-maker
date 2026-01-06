package com.dimasla4ee.playlistmaker.feature.playlist.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.util.LogUtil
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, trackId: Int): Boolean {
        val playlist = repository.getPlaylistById(playlistId)
        return if (playlist.trackIds.contains(trackId)) {
            LogUtil.d("PlaylistInteractorImpl", "Track already in playlist")
            false
        } else {
            LogUtil.d("PlaylistInteractorImpl", "Adding track to playlist: $playlistId, $trackId")
            repository.addTrackToPlaylist(playlistId, trackId)
            true
        }
    }
}