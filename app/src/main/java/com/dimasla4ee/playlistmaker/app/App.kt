package com.dimasla4ee.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dimasla4ee.playlistmaker.app.di.dataModule
import com.dimasla4ee.playlistmaker.app.di.domainModule
import com.dimasla4ee.playlistmaker.app.di.presentationModule
import com.dimasla4ee.playlistmaker.app.di.repositoryModule
import com.dimasla4ee.playlistmaker.app.di.viewModelModule
import com.dimasla4ee.playlistmaker.feature.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(viewModelModule, repositoryModule, domainModule, dataModule, presentationModule)
        }

        val isDarkThemeEnabled = settingsInteractor.isDarkThemeEnabled()

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}