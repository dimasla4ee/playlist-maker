package com.dimasla4ee.playlistmaker.feature.playlists.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.dimasla4ee.playlistmaker.feature.favorite.data.entity.TrackEntity
import com.dimasla4ee.playlistmaker.feature.playlists.data.entity.PlaylistEntity
import com.dimasla4ee.playlistmaker.feature.playlists.data.entity.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist ORDER BY createdAt DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist WHERE id = :id")
    suspend fun getPlaylistById(id: Int): PlaylistEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTrackEntity)

    @Transaction
    suspend fun addTrackToPlaylist(track: TrackEntity, playlistId: Int) {
        insertTrack(track)
        val position = getNextPosition(playlistId)
        insertPlaylistTrack(PlaylistTrackEntity(playlistId, track.id, position))
    }

    @Query("SELECT COALESCE(MAX(position), 0) + 1 FROM playlist_track WHERE playlistId = :playlistId")
    suspend fun getNextPosition(playlistId: Int): Int

    @Query("DELETE FROM playlist_track WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int)

    @Query("""
        SELECT track.* FROM track
        JOIN playlist_track ON track.id = playlist_track.trackId
        WHERE playlist_track.playlistId = :playlistId
        ORDER BY playlist_track.addedAt DESC
    """)
    fun getTracksForPlaylist(playlistId: Int): Flow<List<TrackEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM playlist_track WHERE playlistId = :playlistId AND trackId = :trackId)")
    suspend fun isTrackInPlaylist(playlistId: Int, trackId: Int): Boolean

    @Transaction
    suspend fun deletePlaylistWithTracks(playlist: PlaylistEntity) {
        deleteTracksFromPlaylist(playlist.id)
        deletePlaylist(playlist)
    }

    @Query("DELETE FROM playlist_track WHERE playlistId = :playlistId")
    suspend fun deleteTracksFromPlaylist(playlistId: Int)

    @Query("SELECT COUNT(*) FROM playlist_track WHERE playlistId = :playlistId")
    suspend fun getTrackCountForPlaylist(playlistId: Int): Int

}
