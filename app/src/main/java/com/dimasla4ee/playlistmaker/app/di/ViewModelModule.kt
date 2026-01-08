package com.dimasla4ee.playlistmaker.app.di

import android.media.MediaPlayer
import com.dimasla4ee.playlistmaker.core.data.storage.ImageStorageManager
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractor
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.viewmodel.FavoriteViewModel
import com.dimasla4ee.playlistmaker.feature.playlist.presentation.viewmodel.PlaylistsViewModel
import com.dimasla4ee.playlistmaker.feature.player.presentation.TrackPlayerViewModel
import com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel.MediaPlayerViewModel
import com.dimasla4ee.playlistmaker.feature.playlist.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.playlist.presentation.viewmodel.NewPlaylistViewModel
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchHistoryInteractor
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchTracksUseCase
import com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel.SearchViewModel
import com.dimasla4ee.playlistmaker.feature.settings.domain.SettingsInteractor
import com.dimasla4ee.playlistmaker.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            settingsInteractor = get<SettingsInteractor>(),
        )
    }

    viewModel<SearchViewModel> {
        SearchViewModel(
            searchHistoryInteractor = get<SearchHistoryInteractor>(),
            searchTracksUseCase = get<SearchTracksUseCase>()
        )
    }

    viewModel<MediaPlayerViewModel> { (sourceUrl: String) ->
        MediaPlayerViewModel(
            sourceUrl = sourceUrl,
            mediaPlayer = get<MediaPlayer>()
        )
    }

    viewModel<PlaylistsViewModel> {
        PlaylistsViewModel(
            playlistInteractor = get<PlaylistInteractor>()
        )
    }

    viewModel<FavoriteViewModel> {
        FavoriteViewModel(
            favoriteInteractor = get<FavoriteInteractor>()
        )
    }

    viewModel<TrackPlayerViewModel> { (track: Track) ->
        TrackPlayerViewModel(
            favoriteInteractor = get<FavoriteInteractor>(),
            playlistInteractor = get<PlaylistInteractor>(),
            track = track
        )
    }

    viewModel<NewPlaylistViewModel> {
        NewPlaylistViewModel(
            playlistInteractor = get<PlaylistInteractor>(),
            imageStorageManager = get<ImageStorageManager>()
        )
    }

}