package com.dimasla4ee.playlistmaker.feature.playlists.data.converter

import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.feature.playlists.data.entity.PlaylistEntity

class PlaylistDbConverter {

    fun map(playlist: Playlist): PlaylistEntity = PlaylistEntity(
        id = playlist.id,
        name = playlist.name,
        description = playlist.description,
        coverPath = playlist.coverPath,
        createdAt = playlist.createdAt
    )

    fun map(playlistEntity: PlaylistEntity, trackCount: Int): Playlist = Playlist(
        id = playlistEntity.id,
        name = playlistEntity.name,
        description = playlistEntity.description,
        coverPath = playlistEntity.coverPath,
        createdAt = playlistEntity.createdAt,
        trackCount = trackCount,
        tracks = emptyList()
    )

}
