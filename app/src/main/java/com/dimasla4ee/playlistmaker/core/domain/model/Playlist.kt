package com.dimasla4ee.playlistmaker.core.domain.model

data class Playlist(
    val name: String,
    val description: String,
    val coverPath: String,
    val trackIds: List<Track> = emptyList(),
    val tracksCount: Int = 0
)