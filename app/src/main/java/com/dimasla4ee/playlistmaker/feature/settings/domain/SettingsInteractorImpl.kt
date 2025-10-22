package com.dimasla4ee.playlistmaker.feature.settings.domain

import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.feature.settings.domain.repository.SettingsRepository

class SettingsInteractorImpl(
    private val repository: SettingsRepository
) : SettingsInteractor {

    override fun isDarkThemeEnabled(): Boolean =
        (repository.getDarkThemeEnabled() as Resource.Success<Boolean>).data

    override fun setAppTheme(useDarkTheme: Boolean) = repository.saveTheme(useDarkTheme)
}