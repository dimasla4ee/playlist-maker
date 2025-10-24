package com.dimasla4ee.playlistmaker.app.di

import android.media.MediaPlayer
import org.koin.dsl.module

val presentationModule = module {
    factory {
        MediaPlayer()
    }
}