package com.dimasla4ee.playlistmaker.app.creator

import android.content.Context
import com.dimasla4ee.playlistmaker.core.data.local.PrefsStorageClient
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.util.Keys
import com.dimasla4ee.playlistmaker.feature.search.data.repository.SearchHistoryRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.search.data.repository.TrackSearchRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.SearchHistoryRepository
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.TrackSearchRepository
import com.dimasla4ee.playlistmaker.feature.search.domain.usecase.SearchHistoryInteractor
import com.dimasla4ee.playlistmaker.feature.search.domain.usecase.SearchHistoryInteractorImpl
import com.dimasla4ee.playlistmaker.feature.search.domain.usecase.SearchTracksUseCase
import com.dimasla4ee.playlistmaker.feature.settings.data.repository.SettingsRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.settings.domain.repository.SettingsRepository
import com.dimasla4ee.playlistmaker.feature.settings.domain.usecase.SettingsInteractorImpl
import kotlinx.serialization.builtins.ListSerializer

object Creator {

    private fun getTracksRepository(): TrackSearchRepository = TrackSearchRepositoryImpl()

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(
            PrefsStorageClient(
                context = context,
                prefsName = Keys.SEARCH_PREFERENCES,
                dataKey = Keys.Preference.SEARCH_HISTORY,
                serializer = ListSerializer(Track.serializer())
            )
        )

    private fun getSettingsRepository(context: Context): SettingsRepository =
        SettingsRepositoryImpl(
            PrefsStorageClient(
                context = context,
                prefsName = Keys.APP_PREFERENCES,
                dataKey = Keys.Preference.DARK_THEME
            )
        )

    fun provideSearchTracksUseCase(): SearchTracksUseCase =
        SearchTracksUseCase(getTracksRepository())

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor =
        SearchHistoryInteractorImpl(getSearchHistoryRepository(context))

    fun provideSettingsInteractor(context: Context): SettingsInteractorImpl =
        SettingsInteractorImpl(getSettingsRepository(context))
}