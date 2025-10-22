package com.dimasla4ee.playlistmaker.feature.search.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
) : SearchHistoryInteractor {

    override fun getSearchHistory(): List<Track> =
        (repository.getHistory() as Resource.Success<List<Track>>).data

    override fun saveHistory(tracks: List<Track>) {
        repository.saveHistory(tracks)
    }
}