package com.dimasla4ee.playlistmaker.feature.settings.domain

interface SettingsInteractor {

    fun isDarkThemeEnabled(): Boolean
    fun setAppTheme(useDarkTheme: Boolean)
}