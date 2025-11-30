package com.dimasla4ee.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dimasla4ee.playlistmaker.app.di.DataModule
import com.dimasla4ee.playlistmaker.app.di.DomainModule
import com.dimasla4ee.playlistmaker.app.di.PresentationModule
import com.dimasla4ee.playlistmaker.app.di.RepositoryModule
import com.dimasla4ee.playlistmaker.app.di.ViewModelModule
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
            modules(ViewModelModule, RepositoryModule, DomainModule, DataModule, PresentationModule)
        }

        val isDarkThemeEnabled = settingsInteractor.isDarkThemeEnabled()

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}