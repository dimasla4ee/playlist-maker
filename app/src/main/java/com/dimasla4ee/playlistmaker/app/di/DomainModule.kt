package com.dimasla4ee.playlistmaker.app.di

import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractor
import com.dimasla4ee.playlistmaker.feature.favorite.domain.FavoriteInteractorImpl
import com.dimasla4ee.playlistmaker.feature.playlist.domain.PlaylistInteractor
import com.dimasla4ee.playlistmaker.feature.playlist.domain.PlaylistInteractorImpl
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchHistoryInteractor
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchHistoryInteractorImpl
import com.dimasla4ee.playlistmaker.feature.search.domain.SearchTracksUseCase
import com.dimasla4ee.playlistmaker.feature.settings.domain.SettingsInteractor
import com.dimasla4ee.playlistmaker.feature.settings.domain.SettingsInteractorImpl
import org.koin.dsl.module

val domainModule = module {

    factory<SettingsInteractor> {
        SettingsInteractorImpl(
            repository = get()
        )
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(
            repository = get()
        )
    }

    factory<SearchTracksUseCase> {
        SearchTracksUseCase(
            repository = get()
        )
    }

    factory<FavoriteInteractor> {
        FavoriteInteractorImpl(
            repository = get()
        )
    }

    factory<PlaylistInteractor> {
        PlaylistInteractorImpl(
            repository = get()
        )
    }

}