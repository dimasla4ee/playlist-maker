package com.dimasla4ee.playlistmaker.feature.search.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    val trackId: Int? = null,
    val trackName: String? = null,
    val artistName: String? = null,
    val country: String? = null,
    val collectionName: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String? = null,
    val trackTimeMillis: Long? = null,
    val artworkUrl100: String? = null,
    val previewUrl: String? = null
)