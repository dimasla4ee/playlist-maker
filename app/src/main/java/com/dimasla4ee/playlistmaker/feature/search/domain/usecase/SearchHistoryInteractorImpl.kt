package com.dimasla4ee.playlistmaker.feature.search.domain.usecase

import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {

    override fun getSearchHistory(consumer: SearchHistoryInteractor.HistoryConsumer) {
        val searchHistory = (repository.getHistory() as Resource.Success<List<Track>>).data
        consumer.consume(searchHistory)
    }

    override fun saveHistory(tracks: List<Track>) {
        repository.saveHistory(tracks)
    }
}