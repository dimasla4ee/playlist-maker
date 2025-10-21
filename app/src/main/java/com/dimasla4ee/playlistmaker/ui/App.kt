package com.dimasla4ee.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dimasla4ee.playlistmaker.creator.Creator
import com.dimasla4ee.playlistmaker.domain.use_case.SettingsInteractor
import com.dimasla4ee.playlistmaker.util.Keys

class App : Application() {

    private lateinit var settingsInteractor: SettingsInteractor
    val isDarkThemeEnabled: Boolean
        get() = settingsInteractor.isDarkThemeEnabled()

    override fun onCreate() {
        super.onCreate()
        Creator.setSettingsPrefs(getSharedPreferences(Keys.APP_PREFERENCES, MODE_PRIVATE))
        settingsInteractor = Creator.provideSettingsInteractor()
        setAppTheme(isDarkThemeEnabled)
    }

    fun setAppTheme(useDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (useDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        settingsInteractor.setAppTheme(useDarkTheme)
    }
}