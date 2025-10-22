package com.dimasla4ee.playlistmaker.feature.settings.domain.usecase

import com.dimasla4ee.playlistmaker.core.domain.model.Resource
import com.dimasla4ee.playlistmaker.feature.settings.domain.repository.SettingsRepository

class SettingsInteractorImpl(
    private val repository: SettingsRepository
) : SettingsInteractor {

    override fun isDarkThemeEnabled(consumer: SettingsInteractor.SettingsConsumer) {
        val isDarkTheme = (repository.getDarkThemeEnabled() as Resource.Success<Boolean>).data
        consumer.consume(isDarkTheme)
    }

    override fun setAppTheme(useDarkTheme: Boolean) = repository.saveTheme(useDarkTheme)
}