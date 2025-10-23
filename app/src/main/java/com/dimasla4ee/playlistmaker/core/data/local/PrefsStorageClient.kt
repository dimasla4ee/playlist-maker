package com.dimasla4ee.playlistmaker.core.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent.inject

class PrefsStorageClient<T>(
    context: Context,
    prefsName: String,
    private val dataKey: String,
    private val serializer: KSerializer<T>? = null
) : StorageClient<T> {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        prefsName, MODE_PRIVATE
    )

    val json: Json by inject(Json::class.java)

    override fun getData(): T? = when (serializer) {
        null -> {
            prefs.all[dataKey] as T?
        }

        else -> {
            val dataJson = prefs.getString(dataKey, null) ?: return null
            json.decodeFromString(serializer, dataJson)
        }
    }

    override fun saveData(data: T) {
        prefs.edit {
            when (serializer) {
                null -> when (data) {
                    is Boolean -> putBoolean(dataKey, data)
                    is Int -> putInt(dataKey, data)
                    is Float -> putFloat(dataKey, data)
                    is Long -> putLong(dataKey, data)
                    is String -> putString(dataKey, data)
                    else -> error("Unsupported primitive type")
                }

                else -> putString(dataKey, json.encodeToString(serializer, data))
            }
        }
    }
}