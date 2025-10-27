package com.dimasla4ee.playlistmaker.app.di

import android.media.MediaPlayer
import org.koin.dsl.module

val PresentationModule = module {
    factory {
        MediaPlayer()
    }
}