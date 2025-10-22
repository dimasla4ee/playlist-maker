package com.dimasla4ee.playlistmaker.feature.search.data.repository

import com.dimasla4ee.playlistmaker.core.data.mapper.TrackMapper
import com.dimasla4ee.playlistmaker.core.data.network.RetrofitNetworkClient
import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.search.data.model.TrackSearchRequest
import com.dimasla4ee.playlistmaker.feature.search.data.model.TrackSearchResponse
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.TrackSearchRepository

class TrackSearchRepositoryImpl : TrackSearchRepository {

    override fun searchTracks(
        query: String
    ): Resource<List<Track>> {
        val response = RetrofitNetworkClient.doRequest(TrackSearchRequest(query))

        return when (response.resultCode) {
            0 -> {
                Resource.Failure("No internet connection")
            }

            200 -> {
                Resource.Success(
                    (response as TrackSearchResponse).results.mapNotNull { trackDto ->
                        TrackMapper.map(trackDto)
                    }
                )
            }

            else -> {
                Resource.Failure("Something went wrong")
            }
        }
    }
}