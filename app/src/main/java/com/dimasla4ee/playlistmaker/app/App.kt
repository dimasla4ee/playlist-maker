package com.dimasla4ee.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dimasla4ee.playlistmaker.app.creator.Creator
import com.dimasla4ee.playlistmaker.feature.settings.domain.usecase.SettingsInteractor.SettingsConsumer

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val settingsInteractor = Creator.provideSettingsInteractor(this)

        settingsInteractor.isDarkThemeEnabled(
            object : SettingsConsumer {
                override fun consume(isDarkThemeEnabled: Boolean) {
                    AppCompatDelegate.setDefaultNightMode(
                        if (isDarkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                    )
                }
            }
        )
    }
}