package com.dimasla4ee.playlistmaker.domain.repository

import com.dimasla4ee.playlistmaker.domain.model.Resource
import com.dimasla4ee.playlistmaker.domain.model.Track

interface SearchHistoryRepository {

    fun saveHistory(tracks: List<Track>)
    fun getHistory(): Resource<List<Track>>
}