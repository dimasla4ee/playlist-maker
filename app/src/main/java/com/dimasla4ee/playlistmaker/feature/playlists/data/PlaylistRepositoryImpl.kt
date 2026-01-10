package com.dimasla4ee.playlistmaker.feature.playlists.data

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.favorite.data.converter.TrackDbConverter
import com.dimasla4ee.playlistmaker.feature.playlists.data.converter.PlaylistDbConverter
import com.dimasla4ee.playlistmaker.feature.playlists.data.dao.PlaylistDao
import com.dimasla4ee.playlistmaker.feature.playlists.data.entity.PlaylistEntity
import com.dimasla4ee.playlistmaker.feature.playlists.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistConverter: PlaylistDbConverter,
    private val trackConverter: TrackDbConverter
) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(playlist.toEntity())
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = playlistDao.getAllPlaylists()
        .distinctUntilChanged()
        .map { entities ->
            entities.map { entity ->
                val trackCount = playlistDao.getTrackCountForPlaylist(entity.id)
                entity.toDomain(trackCount)
            }
        }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(playlist.toEntity())
    }

    override suspend fun getPlaylistById(id: Int): Playlist {
        val entity = playlistDao.getPlaylistById(id)
            ?: throw IllegalArgumentException("Playlist not found")
        val trackCount = playlistDao.getTrackCountForPlaylist(id)
        return entity.toDomain(trackCount)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylistWithTracks(playlist.toEntity())
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, track: Track) {
        playlistDao.addTrackToPlaylist(trackConverter.map(track), playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        playlistDao.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override fun getTracksForPlaylist(playlistId: Int): Flow<List<Track>> =
        playlistDao.getTracksForPlaylist(playlistId).map { entities ->
            entities.map { trackConverter.map(it) }
        }

    override suspend fun isTrackInPlaylist(playlistId: Int, trackId: Int): Boolean {
        return playlistDao.isTrackInPlaylist(playlistId, trackId)
    }

    private fun Playlist.toEntity() = playlistConverter.map(this)
    private fun PlaylistEntity.toDomain(trackCount: Int) = playlistConverter.map(this, trackCount)

}
