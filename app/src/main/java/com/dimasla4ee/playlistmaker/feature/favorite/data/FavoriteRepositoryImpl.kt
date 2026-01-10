package com.dimasla4ee.playlistmaker.feature.favorite.data

import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.favorite.data.converter.TrackDbConverter
import com.dimasla4ee.playlistmaker.feature.favorite.data.dao.FavoriteDao
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao,
    private val trackDbConverter: TrackDbConverter
) : FavoriteRepository {

    override fun favoriteTracks(): Flow<List<Track>> =
        favoriteDao.getFavoriteTracks().map { entities ->
            entities.map { trackDbConverter.map(it) }
        }

    override suspend fun getTrackById(trackId: Int): Track? {
        val trackEntity = favoriteDao.getTrackById(trackId) ?: return null
        return trackDbConverter.map(trackEntity)
    }

    override suspend fun insertTrack(track: Track) {
        favoriteDao.addTrackToFavorites(trackDbConverter.map(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        favoriteDao.deleteFavorite(trackId)
    }

    override suspend fun isFavorite(trackId: Int): Boolean {
        return favoriteDao.isFavorite(trackId)
    }

}
