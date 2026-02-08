package com.dimasla4ee.playlistmaker.feature.playlists.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun getPlaylistById(id: Int): Playlist {
        return repository.getPlaylistById(id)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        return if (repository.isTrackInPlaylist(playlist.id, track.id)) {
            false
        } else {
            repository.addTrackToPlaylist(playlist.id, track)
            true
        }
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        repository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override fun getTracksForPlaylist(playlistId: Int): Flow<List<Track>> {
        return repository.getTracksForPlaylist(playlistId)
    }
}
