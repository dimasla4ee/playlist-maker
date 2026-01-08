package com.dimasla4ee.playlistmaker.feature.search.data

import com.dimasla4ee.playlistmaker.feature.search.data.mapper.TrackDtoToDomainMapper
import com.dimasla4ee.playlistmaker.core.data.network.NetworkClient
import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.core.domain.model.ResultCode
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.search.data.model.TrackSearchRequest
import com.dimasla4ee.playlistmaker.feature.search.data.model.TrackSearchResponse
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.TrackSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackSearchRepositoryImpl(
    private val networkClient: NetworkClient
) : TrackSearchRepository {

    override fun searchTracks(
        query: String
    ): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(query))
        val resource = when (response.resultCode) {
            ResultCode.NoInternet -> {
                Resource.Failure("No Internet connection")
            }

            ResultCode.Ok -> {
                Resource.Success(
                    (response as TrackSearchResponse).results.mapNotNull { trackDto ->
                        TrackDtoToDomainMapper.map(trackDto)
                    }
                )
            }

            else -> {
                Resource.Failure("Something went wrong")
            }
        }

        emit(resource)
    }
}