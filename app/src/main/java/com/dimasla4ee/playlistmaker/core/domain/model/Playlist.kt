package com.dimasla4ee.playlistmaker.core.domain.model

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String?,
    val coverPath: String,
    val tracks: List<Track> = emptyList(),
    val trackCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {

    val duration: Long
        get() = tracks.sumOf { it.duration }

}
