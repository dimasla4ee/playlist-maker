package com.dimasla4ee.playlistmaker.feature.search.domain.repository

import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.core.domain.model.Track

interface SearchHistoryRepository {

    fun saveHistory(tracks: List<Track>)
    fun getHistory(): Resource<List<Track>>
}