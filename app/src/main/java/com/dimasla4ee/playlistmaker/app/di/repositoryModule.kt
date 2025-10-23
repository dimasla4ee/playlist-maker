package com.dimasla4ee.playlistmaker.app.di

import com.dimasla4ee.playlistmaker.core.data.local.PrefsStorageClient
import com.dimasla4ee.playlistmaker.core.data.local.StorageClient
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.util.Keys
import com.dimasla4ee.playlistmaker.feature.search.data.repository.SearchHistoryRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.search.data.repository.TrackSearchRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.SearchHistoryRepository
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.TrackSearchRepository
import com.dimasla4ee.playlistmaker.feature.settings.data.repository.SettingsRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.settings.domain.repository.SettingsRepository
import kotlinx.serialization.builtins.ListSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<StorageClient<Boolean>>(named("theme_storage")) {
        PrefsStorageClient(
            context = androidContext(),
            prefsName = Keys.APP_PREFERENCES,
            dataKey = Keys.Preference.DARK_THEME
        )
    }

    single<StorageClient<List<Track>>>(named("search_history_storage")) {
        PrefsStorageClient(
            context = androidContext(),
            prefsName = Keys.SEARCH_PREFERENCES,
            dataKey = Keys.Preference.SEARCH_HISTORY,
            serializer = ListSerializer(Track.serializer())
        )
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get<StorageClient<Boolean>>(named("theme_storage")))
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(named("search_history_storage")))
    }

    factory<TrackSearchRepository> {
        TrackSearchRepositoryImpl(get())
    }
}