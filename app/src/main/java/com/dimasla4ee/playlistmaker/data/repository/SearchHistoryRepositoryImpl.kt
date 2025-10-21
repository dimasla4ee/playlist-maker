package com.dimasla4ee.playlistmaker.data.repository

import com.dimasla4ee.playlistmaker.data.local.StorageClient
import com.dimasla4ee.playlistmaker.domain.model.Resource
import com.dimasla4ee.playlistmaker.domain.model.Track
import com.dimasla4ee.playlistmaker.domain.repository.SearchHistoryRepository

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

