package com.dimasla4ee.playlistmaker.feature.favorite.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "track")
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

@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey val trackId: Int,
    val addedAt: Long = System.currentTimeMillis()
)