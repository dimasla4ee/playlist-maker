package com.dimasla4ee.playlistmaker.feature.playlists.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String?,
    val coverPath: String,
    val createdAt: Long
)

@Entity(
    tableName = "playlist_track",
    primaryKeys = ["playlistId", "trackId"]
)
data class PlaylistTrackEntity(
    val playlistId: Int,
    val trackId: Int,
    val position: Int,
    val addedAt: Long = System.currentTimeMillis()
)
