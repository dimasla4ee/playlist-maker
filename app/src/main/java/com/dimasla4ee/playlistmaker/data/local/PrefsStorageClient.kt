package com.dimasla4ee.playlistmaker.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.dimasla4ee.playlistmaker.util.Keys
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.LocalDate

class PrefsStorageClient<T>(
    context: Context,
    private val dataKey: String,
    private val serializer: KSerializer<T>
) : StorageClient<T> {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        Keys.SEARCH_PREFERENCES, MODE_PRIVATE
    )

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
        serializersModule = SerializersModule {
            contextual(LocalDate::class, LocalDateSerializer)
        }
    }

    override fun getData(): T? {
        val dataJson = prefs.getString(dataKey, null) ?: return null
        return json.decodeFromString(serializer, dataJson)
    }

    override fun saveData(data: T) {
        prefs.edit {
            putString(
                dataKey,
                json.encodeToString(serializer, data)
            )
        }
    }
}