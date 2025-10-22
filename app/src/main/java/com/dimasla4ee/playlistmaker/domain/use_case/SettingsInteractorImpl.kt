package com.dimasla4ee.playlistmaker.domain.use_case

import com.dimasla4ee.playlistmaker.domain.model.Resource
import com.dimasla4ee.playlistmaker.domain.repository.SettingsRepository

class SettingsInteractorImpl(
    private val repository: SettingsRepository
) : SettingsInteractor {

    override fun isDarkThemeEnabled(consumer: SettingsInteractor.SettingsConsumer) {
        val isDarkTheme = (repository.getDarkThemeEnabled() as Resource.Success<Boolean>).data
        consumer.consume(isDarkTheme)
    }

    override fun setAppTheme(useDarkTheme: Boolean) = repository.saveTheme(useDarkTheme)
}