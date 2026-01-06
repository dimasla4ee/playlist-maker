package com.dimasla4ee.playlistmaker.core.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class TrackIdsConverter {
    private val json = Json

    @TypeConverter
    fun fromTrackIds(trackIds: List<Int>): String {
        return json.encodeToString(ListSerializer(Int.serializer()), trackIds)
    }

    @TypeConverter
    fun toTrackIds(trackIdsJson: String): List<Int> {
        return json.decodeFromString(ListSerializer(Int.serializer()), trackIdsJson)
    }
}