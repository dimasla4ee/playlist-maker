package com.dimasla4ee.playlistmaker.feature.settings.domain.repository

import com.dimasla4ee.playlistmaker.core.domain.model.Resource

interface SettingsRepository {

    fun saveTheme(useDarkTheme: Boolean)
    fun getDarkThemeEnabled(): Resource<Boolean>
}