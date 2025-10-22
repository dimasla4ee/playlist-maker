package com.dimasla4ee.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dimasla4ee.playlistmaker.app.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val settingsInteractor = Creator.provideSettingsInteractor(this)

        val isDarkThemeEnabled = settingsInteractor.isDarkThemeEnabled()
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}