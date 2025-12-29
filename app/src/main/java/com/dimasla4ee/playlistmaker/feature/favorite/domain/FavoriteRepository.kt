package com.dimasla4ee.playlistmaker.feature.favorite.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun favoriteTracks(): Flow<List<Track>>
    suspend fun getTrackById(trackId: Int): Track?
    suspend fun insertTrack(track: Track)
    suspend fun deleteTrack(trackId: Int)

}
