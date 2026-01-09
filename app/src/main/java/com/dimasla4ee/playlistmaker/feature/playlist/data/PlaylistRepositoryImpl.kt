package com.dimasla4ee.playlistmaker.feature.playlist.data

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.feature.playlist.data.converter.PlaylistDbConverter
import com.dimasla4ee.playlistmaker.feature.playlist.data.dao.PlaylistDao
import com.dimasla4ee.playlistmaker.feature.playlist.data.entity.PlaylistEntity
import com.dimasla4ee.playlistmaker.feature.playlist.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistConverter: PlaylistDbConverter
) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistDao.createPlaylist(playlist.toEntity())
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = playlistDao.getAllPlaylists()
        .distinctUntilChanged()
        .map { entities ->
            entities.map(playlistConverter::map)
        }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(playlist.toEntity())
    }

    override suspend fun getPlaylistById(id: Int): Playlist =
        playlistDao.getPlaylistById(id).toDomain()


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

    private fun Playlist.toEntity() = playlistConverter.map(this)
    private fun PlaylistEntity.toDomain() = playlistConverter.map(this)

}