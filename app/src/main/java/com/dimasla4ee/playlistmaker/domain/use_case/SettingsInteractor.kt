package com.dimasla4ee.playlistmaker.domain.use_case

interface SettingsInteractor {

    fun isDarkThemeEnabled(consumer: SettingsConsumer)
    fun setAppTheme(useDarkTheme: Boolean)

    interface SettingsConsumer {

        fun consume(isDarkThemeEnabled: Boolean)
    }
}