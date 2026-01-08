package com.dimasla4ee.playlistmaker.app.di

import androidx.room.Room
import com.dimasla4ee.playlistmaker.core.config.ApiConfig
import com.dimasla4ee.playlistmaker.core.data.storage.ImageStorageManager
import com.dimasla4ee.playlistmaker.core.data.storage.LocalDateSerializer
import com.dimasla4ee.playlistmaker.core.data.database.AppDatabase
import com.dimasla4ee.playlistmaker.core.data.database.DatabaseConfig
import com.dimasla4ee.playlistmaker.core.data.network.ItunesService
import com.dimasla4ee.playlistmaker.core.data.network.NetworkClient
import com.dimasla4ee.playlistmaker.core.data.network.NetworkConstants
import com.dimasla4ee.playlistmaker.core.data.network.RetrofitNetworkClient
import com.dimasla4ee.playlistmaker.feature.favorite.data.dao.FavoriteDao
import com.dimasla4ee.playlistmaker.feature.playlist.data.dao.PlaylistDao
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.time.LocalDate

val dataModule = module {

    single<NetworkClient> {
        RetrofitNetworkClient(
            service = get<ItunesService>()
        )
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

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(
                get<Json>().asConverterFactory(NetworkConstants.JSON_MEDIA_TYPE)
            )
            .build()
    }

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DatabaseConfig.NAME
        ).fallbackToDestructiveMigration(false).build()
    }

    single<FavoriteDao> {
        get<AppDatabase>().favoriteDao()
    }

    single<PlaylistDao> {
        get<AppDatabase>().playlistDao()
    }

    single<ImageStorageManager> {
        ImageStorageManager(
            context = androidContext()
        )
    }

}