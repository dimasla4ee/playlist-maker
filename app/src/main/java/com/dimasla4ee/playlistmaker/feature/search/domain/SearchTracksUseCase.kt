package com.dimasla4ee.playlistmaker.feature.search.domain

import com.dimasla4ee.playlistmaker.core.domain.consumer.Consumer
import com.dimasla4ee.playlistmaker.core.domain.consumer.ConsumerData
import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.TrackSearchRepository
import java.util.concurrent.Executors

class SearchTracksUseCase(
    private val trackSearchRepository: TrackSearchRepository
) {

    private val executor = Executors.newSingleThreadExecutor()

    fun execute(
        query: String,
        consumer: Consumer<List<Track>>
    ) {
        executor.execute {
            val tracksResource = trackSearchRepository.searchTracks(query)

            consumer.consume(
                when (tracksResource) {
                    is Resource.Failure -> {
                        ConsumerData.Error(tracksResource.message)
                    }

                    is Resource.Success -> {
                        ConsumerData.Data(tracksResource.data)
                    }
                }
            )
        }
    }
}