package com.dimasla4ee.playlistmaker.feature.media_library.presentation

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.feature.new_playlist.data.entity.PlaylistEntity

class PlaylistDbConverter {

    fun map(playlist: Playlist): PlaylistEntity = PlaylistEntity(
        id = playlist.id,
        name = playlist.name,
        description = playlist.description,
        coverPath = playlist.coverPath,
        trackIds = playlist.trackIds,
        tracksCount = playlist.tracksCount
    )

    fun map(playlist: PlaylistEntity): Playlist = Playlist(
        id = playlist.id,
        name = playlist.name,
        description = playlist.description,
        coverPath = playlist.coverPath,
        trackIds = playlist.trackIds,
        tracksCount = playlist.tracksCount
    )

}