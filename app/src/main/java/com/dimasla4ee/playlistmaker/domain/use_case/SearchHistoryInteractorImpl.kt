package com.dimasla4ee.playlistmaker.domain.use_case

import com.dimasla4ee.playlistmaker.domain.model.Resource
import com.dimasla4ee.playlistmaker.domain.model.Track
import com.dimasla4ee.playlistmaker.domain.repository.SearchHistoryRepository

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