package com.dimasla4ee.playlistmaker.feature.search.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.TrackSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchTracksUseCase(
    private val repository: TrackSearchRepository
) {

    fun execute(
        query: String
    ): Flow<Pair<List<Track>?, String?>> = repository.searchTracks(query).map { resource ->
        when (resource) {
            is Resource.Failure -> {
                null to resource.message
            }

            is Resource.Success -> {
                resource.data to null
            }
        }
    }
}