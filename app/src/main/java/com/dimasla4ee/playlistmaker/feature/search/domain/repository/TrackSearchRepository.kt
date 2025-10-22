package com.dimasla4ee.playlistmaker.feature.search.domain.repository

import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.core.domain.model.Track

interface TrackSearchRepository {

    fun searchTracks(query: String): Resource<List<Track>>
}