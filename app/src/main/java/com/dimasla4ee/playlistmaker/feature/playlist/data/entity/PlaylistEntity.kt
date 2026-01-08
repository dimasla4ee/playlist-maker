package com.dimasla4ee.playlistmaker.feature.playlist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Contextual

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val coverPath: String,
    @Contextual val trackIds: List<Int> = emptyList(),
    val tracksCount: Int = 0
)
