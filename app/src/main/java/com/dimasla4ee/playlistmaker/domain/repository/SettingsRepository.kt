package com.dimasla4ee.playlistmaker.domain.repository

import com.dimasla4ee.playlistmaker.domain.model.Resource

interface SettingsRepository {

    fun saveTheme(useDarkTheme: Boolean)
    fun getDarkThemeEnabled(): Resource<Boolean>
}