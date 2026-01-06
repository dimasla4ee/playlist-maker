package com.dimasla4ee.playlistmaker.feature.playlist.data

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.util.LogUtil
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.PlaylistDbConverter
import com.dimasla4ee.playlistmaker.feature.new_playlist.data.dao.PlaylistDao
import com.dimasla4ee.playlistmaker.feature.playlist.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistDao.createPlaylist(playlistDbConverter.map(playlist))
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { playlists ->
            playlists.map { playlist -> playlistDbConverter.map(playlist) }
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        LogUtil.d("PlaylistRepositoryImpl", "updatePlaylist: $playlist")
        playlistDao.updatePlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun getPlaylistById(id: Int): Playlist {
        return playlistDbConverter.map(playlistDao.getPlaylistById(id))
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, trackId: Int) {
        val playlist = getPlaylistById(playlistId)
        val updatedTrackIds = playlist.trackIds.toMutableList().apply {
            add(trackId)
        }
        val updatedPlaylist = playlist.copy(
            trackIds = updatedTrackIds,
            tracksCount = updatedTrackIds.size
        )
        updatePlaylist(updatedPlaylist)
    }
}