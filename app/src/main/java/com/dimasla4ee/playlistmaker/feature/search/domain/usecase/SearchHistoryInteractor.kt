package com.dimasla4ee.playlistmaker.feature.search.domain.usecase

import com.dimasla4ee.playlistmaker.core.domain.model.Track

interface SearchHistoryInteractor {

    fun getSearchHistory(consumer: HistoryConsumer)
    fun saveHistory(tracks: List<Track>)

    interface HistoryConsumer {

        fun consume(searchHistory: List<Track>)
    }
}