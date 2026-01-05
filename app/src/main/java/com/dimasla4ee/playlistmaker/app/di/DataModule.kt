package com.dimasla4ee.playlistmaker.app.di

import androidx.room.Room
import com.dimasla4ee.playlistmaker.core.data.database.AppDatabase
import com.dimasla4ee.playlistmaker.core.data.local.ImageStorageManager
import com.dimasla4ee.playlistmaker.core.data.local.LocalDateSerializer
import com.dimasla4ee.playlistmaker.core.data.network.ItunesService
import com.dimasla4ee.playlistmaker.core.data.network.NetworkClient
import com.dimasla4ee.playlistmaker.core.data.network.RetrofitNetworkClient
import com.dimasla4ee.playlistmaker.feature.favorite.data.FavoriteDao
import com.dimasla4ee.playlistmaker.feature.new_playlist.data.dao.PlaylistDao
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.time.LocalDate

val DataModule = module {

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<ItunesService> {
        get<Retrofit>().create(ItunesService::class.java)
    }

    single<Json> {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            serializersModule = SerializersModule {
                contextual(LocalDate::class, LocalDateSerializer)
            }
        }
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json; charset=UTF8".toMediaType())
            )
            .build()
    }

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db"
        ).fallbackToDestructiveMigration(false).build()
    }

    single<FavoriteDao> {
        get<AppDatabase>().favoriteDao()
    }

    single<PlaylistDao> {
        get<AppDatabase>().playlistDao()
    }

    single {
        ImageStorageManager(androidContext())
    }

}