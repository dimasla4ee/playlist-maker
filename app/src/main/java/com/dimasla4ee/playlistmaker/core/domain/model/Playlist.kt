package com.dimasla4ee.playlistmaker.core.domain.model

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String,
    val coverPath: String,
    val trackIds: List<Int> = emptyList(),
    val tracksCount: Int = 0
)
