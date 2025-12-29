package com.dimasla4ee.playlistmaker.feature.favorite.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorite")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM favorite WHERE id = :trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity?

}