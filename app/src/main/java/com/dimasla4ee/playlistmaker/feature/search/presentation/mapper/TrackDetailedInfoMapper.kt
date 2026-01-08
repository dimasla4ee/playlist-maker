package com.dimasla4ee.playlistmaker.feature.search.presentation.mapper

import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.utils.toMmSs
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.TrackDetailedInfo

object TrackDetailedInfoMapper {

    private const val ARTWORK_BIG_RESOLUTION_SUFFIX = "512x512bb.jpg"

    fun map(track: Track): TrackDetailedInfo = TrackDetailedInfo(
        title = track.title,
        artist = track.artist,
        country = track.country,
        album = track.album,
        year = track.releaseDate?.year,
        genre = track.genre,
        duration = track.duration.toMmSs(),
        coverUrl = getCover(track.thumbnailUrl),
        audioUrl = track.audioUrl
    )

    private fun getCover(thumbnailUrl: String): String = thumbnailUrl.replaceAfterLast(
        '/', ARTWORK_BIG_RESOLUTION_SUFFIX
    )
}