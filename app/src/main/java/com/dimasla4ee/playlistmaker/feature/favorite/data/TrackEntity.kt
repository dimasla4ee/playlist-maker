package com.dimasla4ee.playlistmaker.feature.favorite.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "favorite")
data class TrackEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val artist: String,
    val country: String,
    val album: String?,
    val releaseDate: LocalDate?,
    val genre: String,
    val duration: Long,
    val thumbnailUrl: String,
    val audioUrl: String
)
