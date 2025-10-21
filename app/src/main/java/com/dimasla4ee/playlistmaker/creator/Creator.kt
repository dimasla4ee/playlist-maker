package com.dimasla4ee.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.dimasla4ee.playlistmaker.data.local.PrefsStorageClient
import com.dimasla4ee.playlistmaker.data.local.SettingsSharedPrefs
import com.dimasla4ee.playlistmaker.data.local.SettingsStorage
import com.dimasla4ee.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.dimasla4ee.playlistmaker.data.repository.SettingsRepositoryImpl
import com.dimasla4ee.playlistmaker.data.repository.TrackSearchRepositoryImpl
import com.dimasla4ee.playlistmaker.domain.model.Track
import com.dimasla4ee.playlistmaker.domain.repository.SearchHistoryRepository
import com.dimasla4ee.playlistmaker.domain.repository.SettingsRepository
import com.dimasla4ee.playlistmaker.domain.repository.TrackSearchRepository
import com.dimasla4ee.playlistmaker.domain.use_case.SearchHistoryInteractor
import com.dimasla4ee.playlistmaker.domain.use_case.SearchHistoryInteractorImpl
import com.dimasla4ee.playlistmaker.domain.use_case.SearchTracksUseCase
import com.dimasla4ee.playlistmaker.domain.use_case.SettingsInteractor
import com.dimasla4ee.playlistmaker.util.Keys
import kotlinx.serialization.builtins.ListSerializer

object Creator {

    private lateinit var settingsSharedPrefs: SharedPreferences

    fun setSettingsPrefs(prefs: SharedPreferences) {
        settingsSharedPrefs = prefs
    }

    private fun getTracksRepository(): TrackSearchRepository = TrackSearchRepositoryImpl()

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(
            PrefsStorageClient(
                context = context,
                dataKey = Keys.Preference.SEARCH_HISTORY,
                serializer = ListSerializer(Track.serializer())
            )
        )

    private fun getSettingsRepository(): SettingsRepository =
        SettingsRepositoryImpl(getSettingsStorage())

    fun getSettingsStorage(): SettingsStorage =
        SettingsSharedPrefs(settingsSharedPrefs)

    fun provideSearchTracksUseCase(): SearchTracksUseCase =
        SearchTracksUseCase(getTracksRepository())

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor =
        SearchHistoryInteractorImpl(getSearchHistoryRepository(context))

    fun provideSettingsInteractor(): SettingsInteractor =
        SettingsInteractor(getSettingsRepository())
}