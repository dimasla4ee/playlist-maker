package com.dimasla4ee.playlistmaker.feature.favorite.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val repository: FavoriteRepository
) : FavoriteInteractor {

    override fun favoriteTracks(): Flow<List<Track>> = repository.favoriteTracks()

    override suspend fun addTrack(track: Track) {
        repository.insertTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        repository.deleteTrack(trackId)
    }

    override suspend fun getTrackById(trackId: Int): Track? = repository.getTrackById(trackId)

}