package com.dimasla4ee.playlistmaker.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dimasla4ee.playlistmaker.core.data.database.converters.LocalDateTypeConverter
import com.dimasla4ee.playlistmaker.feature.favorite.data.dao.FavoriteDao
import com.dimasla4ee.playlistmaker.feature.favorite.data.entity.FavoriteEntity
import com.dimasla4ee.playlistmaker.feature.favorite.data.entity.TrackEntity
import com.dimasla4ee.playlistmaker.feature.playlists.data.dao.PlaylistDao
import com.dimasla4ee.playlistmaker.feature.playlists.data.entity.PlaylistEntity
import com.dimasla4ee.playlistmaker.feature.playlists.data.entity.PlaylistTrackEntity

@Database(
    version = 3,
    entities = [
        TrackEntity::class,
        FavoriteEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class
    ]
)
@TypeConverters(LocalDateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    abstract fun playlistDao(): PlaylistDao

}
