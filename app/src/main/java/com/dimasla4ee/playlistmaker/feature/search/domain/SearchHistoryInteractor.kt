package com.dimasla4ee.playlistmaker.feature.search.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Track

interface SearchHistoryInteractor {

    fun getSearchHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}