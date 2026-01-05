package com.dimasla4ee.playlistmaker.app.di

import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.viewmodel.FavoriteViewModel
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.viewmodel.PlaylistsViewModel
import com.dimasla4ee.playlistmaker.feature.new_playlist.presentation.viewmodel.NewPlaylistViewModel
import com.dimasla4ee.playlistmaker.feature.player.presentation.TrackPlayerViewModel
import com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel.MediaPlayerViewModel
import com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel.SearchViewModel
import com.dimasla4ee.playlistmaker.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel { (sourceUrl: String) ->
        MediaPlayerViewModel(sourceUrl, get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel { (track: Track) ->
        TrackPlayerViewModel(get(), track)
    }

    viewModel {
        NewPlaylistViewModel(get(), get())
    }

}