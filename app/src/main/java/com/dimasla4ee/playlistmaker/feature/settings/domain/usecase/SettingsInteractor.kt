package com.dimasla4ee.playlistmaker.feature.settings.domain.usecase

interface SettingsInteractor {

    fun isDarkThemeEnabled(consumer: SettingsConsumer)
    fun setAppTheme(useDarkTheme: Boolean)

    interface SettingsConsumer {

        fun consume(isDarkThemeEnabled: Boolean)
    }
}