package com.dimasla4ee.playlistmaker.feature.favorite.data

import com.dimasla4ee.playlistmaker.core.data.database.AppDatabase
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteRepository {

    private val dao = appDatabase.favoriteDao()

    override fun favoriteTracks(): Flow<List<Track>> =
        dao.getTracks().map { convertFromTrackEntity(it) }

    override suspend fun getTrackById(trackId: Int): Track? {
        val trackEntity = dao.getTrackById(trackId) ?: return null
        return trackDbConvertor.map(trackEntity)
    }

    override suspend fun insertTrack(track: Track) {
        dao.insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        with(dao) {
            val track = getTrackById(trackId) ?: return
            deleteTrack(track)
        }
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> =
        tracks.map { track ->
            trackDbConvertor.map(track)
        }

}