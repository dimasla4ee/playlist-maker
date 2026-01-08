package com.dimasla4ee.playlistmaker.feature.settings.data

import com.dimasla4ee.playlistmaker.core.data.storage.StorageClient
import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.feature.settings.domain.repository.SettingsRepository

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