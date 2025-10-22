package com.dimasla4ee.playlistmaker.creator

import android.content.Context
import com.dimasla4ee.playlistmaker.data.local.PrefsStorageClient
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
import com.dimasla4ee.playlistmaker.domain.use_case.SettingsInteractorImpl
import com.dimasla4ee.playlistmaker.util.Keys
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