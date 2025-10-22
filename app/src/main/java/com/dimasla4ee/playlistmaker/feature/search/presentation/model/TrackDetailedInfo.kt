package com.dimasla4ee.playlistmaker.feature.search.presentation.model

data class TrackDetailedInfo(
    val title: String,
    val artist: String,
    val country: String,
    val album: String?,
    val year: Int?,
    val genre: String,
    val duration: String,
    val coverUrl: String,
    val audioUrl: String
)