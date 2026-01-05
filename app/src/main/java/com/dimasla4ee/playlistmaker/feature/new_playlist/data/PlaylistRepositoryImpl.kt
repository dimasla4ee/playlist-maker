package com.dimasla4ee.playlistmaker.feature.new_playlist.data

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.PlaylistDbConverter
import com.dimasla4ee.playlistmaker.feature.new_playlist.data.dao.PlaylistDao
import com.dimasla4ee.playlistmaker.feature.new_playlist.data.entity.PlaylistEntity
import com.dimasla4ee.playlistmaker.feature.new_playlist.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConverter.map(playlist)
        playlistDao.createPlaylist(playlistEntity)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> =

        playlistDao.getAllPlaylists().map { convertFromTrackEntity(it) }

    private fun convertFromTrackEntity(playlists: List<PlaylistEntity>): List<Playlist> =
        playlists.map { playlist ->
            playlistDbConverter.map(playlist)
        }

}
