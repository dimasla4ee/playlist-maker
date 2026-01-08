package com.dimasla4ee.playlistmaker.feature.search.data

import com.dimasla4ee.playlistmaker.core.data.storage.StorageClient
import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    val storage: StorageClient<List<Track>>
) : SearchHistoryRepository {

    override fun saveHistory(tracks: List<Track>) {
        storage.saveData(tracks)
    }

    override fun getHistory(): Resource<List<Track>> {
        val tracks = storage.getData() ?: listOf()
        return Resource.Success(tracks)
    }
}

