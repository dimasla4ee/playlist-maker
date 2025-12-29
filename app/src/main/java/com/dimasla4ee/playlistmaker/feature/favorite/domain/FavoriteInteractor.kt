package com.dimasla4ee.playlistmaker.feature.favorite.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    fun favoriteTracks(): Flow<List<Track>>
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(trackId: Int)
    suspend fun getTrackById(trackId: Int): Track?

}
