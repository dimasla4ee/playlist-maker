package com.dimasla4ee.playlistmaker.domain.use_case

import com.dimasla4ee.playlistmaker.domain.model.Track

interface SearchHistoryInteractor {

    fun getSearchHistory(consumer: HistoryConsumer)
    fun saveHistory(tracks: List<Track>)

    interface HistoryConsumer {

        fun consume(searchHistory: List<Track>)
    }
}