package com.dimasla4ee.playlistmaker.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dimasla4ee.playlistmaker.core.data.database.converters.LocalDateTypeConverter
import com.dimasla4ee.playlistmaker.core.data.database.converters.TrackIdsTypeConverter
import com.dimasla4ee.playlistmaker.feature.favorite.data.dao.FavoriteDao
import com.dimasla4ee.playlistmaker.feature.favorite.data.entity.TrackEntity
import com.dimasla4ee.playlistmaker.feature.playlist.data.dao.PlaylistDao
import com.dimasla4ee.playlistmaker.feature.playlist.data.entity.PlaylistEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class])
@TypeConverters(LocalDateTypeConverter::class, TrackIdsTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    abstract fun playlistDao(): PlaylistDao

}
