package com.dimasla4ee.playlistmaker.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dimasla4ee.playlistmaker.feature.favorite.data.FavoriteDao
import com.dimasla4ee.playlistmaker.feature.favorite.data.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

}