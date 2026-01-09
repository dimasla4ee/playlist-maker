package com.dimasla4ee.playlistmaker.feature.favorite.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dimasla4ee.playlistmaker.feature.favorite.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorite ORDER BY addedAt DESC")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM favorite WHERE id = :trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity?

}