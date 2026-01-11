package com.dimasla4ee.playlistmaker.app.di

import android.media.MediaPlayer
import com.dimasla4ee.playlistmaker.core.data.storage.ImageStorageManager
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractor
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.viewmodel.FavoriteViewModel
import com.dimasla4ee.playlistmaker.feature.player.presentation.TrackPlayerViewModel
import com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel.MediaPlayerViewModel
import com.dimasla4ee.playlistmaker.feature.playlists.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.PlaylistDetailedViewModel
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.viewmodel.PlaylistEditViewModel
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.viewmodel.PlaylistListViewModel
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

    viewModel<PlaylistListViewModel> {
        PlaylistListViewModel(
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

    viewModel<PlaylistEditViewModel> { (playlistId: Int?) ->
        PlaylistEditViewModel(
            playlistInteractor = get<PlaylistInteractor>(),
            imageStorageManager = get<ImageStorageManager>(),
            playlistId = if (playlistId == -1) null else playlistId
        )
    }

    viewModel<PlaylistDetailedViewModel> { (playlistId: Int) ->
        PlaylistDetailedViewModel(
            interactor = get<PlaylistInteractor>(),
            playlistId = playlistId
        )
    }

}
