package com.dimasla4ee.playlistmaker.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dimasla4ee.playlistmaker.feature.favorite.data.FavoriteDao
import com.dimasla4ee.playlistmaker.feature.favorite.data.TrackEntity
import com.dimasla4ee.playlistmaker.feature.new_playlist.data.dao.PlaylistDao
import com.dimasla4ee.playlistmaker.feature.new_playlist.data.entity.PlaylistEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class])
@TypeConverters(LocalDateConverter::class, TrackIdsConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    abstract fun playlistDao(): PlaylistDao

}
