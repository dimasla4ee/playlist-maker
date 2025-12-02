package com.dimasla4ee.playlistmaker.feature.search.domain.repository

import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchRepository {

    fun searchTracks(query: String): Flow<Resource<List<Track>>>
}