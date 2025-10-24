package com.dimasla4ee.playlistmaker.app.di

import com.dimasla4ee.playlistmaker.feature.media_library.presentation.viewmodel.FavoriteViewModel
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.viewmodel.PlaylistsViewModel
import com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel.MediaPlayerViewModel
import com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel.SearchViewModel
import com.dimasla4ee.playlistmaker.feature.settings.presentation.viewmodel.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
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
        PlaylistsViewModel()
    }

    viewModel {
        FavoriteViewModel()
    }
}