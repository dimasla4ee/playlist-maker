package com.dimasla4ee.playlistmaker.app.di

import com.dimasla4ee.playlistmaker.core.data.network.NetworkClient
import com.dimasla4ee.playlistmaker.core.data.storage.PrefsStorageClient
import com.dimasla4ee.playlistmaker.core.data.storage.StorageClient
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.utils.KeyConstants
import com.dimasla4ee.playlistmaker.feature.favorite.data.FavoriteRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.favorite.data.converter.TrackDbConverter
import com.dimasla4ee.playlistmaker.feature.favorite.data.dao.FavoriteDao
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteRepository
import com.dimasla4ee.playlistmaker.feature.playlists.data.PlaylistRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.playlists.data.converter.PlaylistDbConverter
import com.dimasla4ee.playlistmaker.feature.playlists.data.dao.PlaylistDao
import com.dimasla4ee.playlistmaker.feature.playlists.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.playlists.domain.PlaylistInteractorImpl
import com.dimasla4ee.playlistmaker.feature.playlists.domain.PlaylistRepository
import com.dimasla4ee.playlistmaker.feature.search.data.SearchHistoryRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.search.data.TrackSearchRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.SearchHistoryRepository
import com.dimasla4ee.playlistmaker.feature.search.domain.repository.TrackSearchRepository
import com.dimasla4ee.playlistmaker.feature.settings.data.SettingsRepositoryImpl
import com.dimasla4ee.playlistmaker.feature.settings.domain.repository.SettingsRepository
import kotlinx.serialization.builtins.ListSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<ThemeStorage>(named<ThemeStorageQualifier>()) {
        PrefsStorageClient(
            context = androidContext(),
            prefsName = KeyConstants.APP_PREFERENCES,
            dataKey = KeyConstants.Preference.DARK_THEME
        )
    }

    single<SearchHistoryStorage>(named<SearchHistoryStorageQualifier>()) {
        PrefsStorageClient(
            context = androidContext(),
            prefsName = KeyConstants.SEARCH_PREFERENCES,
            dataKey = KeyConstants.Preference.SEARCH_HISTORY,
            serializer = ListSerializer(Track.serializer())
        )
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(
            storage = get<ThemeStorage>(named<ThemeStorageQualifier>())
        )
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            storage = get<SearchHistoryStorage>(named<SearchHistoryStorageQualifier>())
        )
    }

    factory<TrackSearchRepository> {
        TrackSearchRepositoryImpl(
            networkClient = get<NetworkClient>()
        )
    }

    factory<TrackDbConverter> {
        TrackDbConverter()
    }

    factory<FavoriteRepository> {
        FavoriteRepositoryImpl(
            favoriteDao = get<FavoriteDao>(),
            trackDbConverter = get<TrackDbConverter>()
        )
    }

    factory<PlaylistRepository> {
        PlaylistRepositoryImpl(
            playlistDao = get<PlaylistDao>(),
            playlistConverter = get<PlaylistDbConverter>(),
            trackConverter = get<TrackDbConverter>()
        )
    }

    factory<PlaylistDbConverter> {
        PlaylistDbConverter()
    }

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(
            repository = get<PlaylistRepository>()
        )
    }

}

private typealias ThemeStorage = StorageClient<Boolean>
private typealias SearchHistoryStorage = StorageClient<List<Track>>
