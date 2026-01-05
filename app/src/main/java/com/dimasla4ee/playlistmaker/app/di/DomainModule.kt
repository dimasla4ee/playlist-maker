package com.dimasla4ee.playlistmaker.app.di

import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractor
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractorImpl
import com.dimasla4ee.playlistmaker.feature.new_playlist.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.new_playlist.domain.PlaylistInteractorImpl
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchHistoryInteractor
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchHistoryInteractorImpl
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchTracksUseCase
import com.dimasla4ee.playlistmaker.feature.settings.domain.SettingsInteractor
import com.dimasla4ee.playlistmaker.feature.settings.domain.SettingsInteractorImpl
import org.koin.dsl.module

val DomainModule = module {

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<SearchTracksUseCase> {
        SearchTracksUseCase(get())
    }

    factory<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

}