package com.dimasla4ee.playlistmaker.feature.favorite.data

import com.dimasla4ee.playlistmaker.core.domain.model.Track

class TrackDbConvertor {

    fun map(track: Track): TrackEntity = TrackEntity(
        id = track.id,
        title = track.title,
        artist = track.artist,
        country = track.country,
        album = track.album,
        releaseDate = track.releaseDate,
        genre = track.genre,
        duration = track.duration,
        thumbnailUrl = track.thumbnailUrl,
        audioUrl = track.audioUrl
    )

    fun map(track: TrackEntity): Track = Track(
        id = track.id,
        title = track.title,
        artist = track.artist,
        country = track.country,
        album = track.album,
        releaseDate = track.releaseDate,
        genre = track.genre,
        duration = track.duration,
        thumbnailUrl = track.thumbnailUrl,
        audioUrl = track.audioUrl
    )

}