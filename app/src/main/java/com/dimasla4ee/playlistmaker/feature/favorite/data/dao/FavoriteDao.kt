package com.dimasla4ee.playlistmaker.feature.favorite.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dimasla4ee.playlistmaker.feature.favorite.data.entity.FavoriteEntity
import com.dimasla4ee.playlistmaker.feature.favorite.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Transaction
    suspend fun addTrackToFavorites(track: TrackEntity) {
        insertTrack(track)
        insertFavorite(FavoriteEntity(trackId = track.id))
    }

    @Query("DELETE FROM favorite WHERE trackId = :trackId")
    suspend fun deleteFavorite(trackId: Int)

    @Query("""
        SELECT track.* FROM track 
        JOIN favorite ON track.id = favorite.trackId 
        ORDER BY favorite.addedAt DESC
    """)
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track WHERE id = :trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM favorite WHERE trackId = :trackId)")
    suspend fun isFavorite(trackId: Int): Boolean

}
