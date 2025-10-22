package com.dimasla4ee.playlistmaker.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Parcelize
@Serializable
data class Track(
    val id: Int,
    val title: String,
    val artist: String,
    val country: String,
    val album: String?,
    @Contextual val releaseDate: LocalDate?,
    val genre: String,
    val duration: Long,
    val thumbnailUrl: String,
    val audioUrl: String
) : Parcelable