package com.dimasla4ee.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.dimasla4ee.playlistmaker.creator.Creator
import com.dimasla4ee.playlistmaker.domain.use_case.SettingsInteractor

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val settingsInteractor = Creator.provideSettingsInteractor(this)

        settingsInteractor.isDarkThemeEnabled(
            object : SettingsInteractor.SettingsConsumer {
                override fun consume(isDarkThemeEnabled: Boolean) {
                    AppCompatDelegate.setDefaultNightMode(
                        if (isDarkThemeEnabled) MODE_NIGHT_YES else MODE_NIGHT_NO
                    )
                }
            }
        )
    }
}