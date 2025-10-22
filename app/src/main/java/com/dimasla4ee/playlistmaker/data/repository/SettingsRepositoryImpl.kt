package com.dimasla4ee.playlistmaker.data.repository

import com.dimasla4ee.playlistmaker.data.local.StorageClient
import com.dimasla4ee.playlistmaker.domain.model.Resource
import com.dimasla4ee.playlistmaker.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val storage: StorageClient<Boolean>
) : SettingsRepository {

    override fun saveTheme(useDarkTheme: Boolean) {
        storage.saveData(useDarkTheme)
    }

    override fun getDarkThemeEnabled(): Resource<Boolean> {
        val idDarkThemeEnabled = storage.getData() ?: false
        return Resource.Success(idDarkThemeEnabled)
    }
}

