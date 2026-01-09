package com.dimasla4ee.playlistmaker.feature.player.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist

class PlaylistBottomSheetAdapter(
    private val onPlaylistClickListener: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistBottomSheetViewHolder>() {

    var playlists = listOf<Playlist>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistBottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item_small, parent, false)
        return PlaylistBottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { onPlaylistClickListener(playlist) }
    }

    override fun getItemCount() = playlists.size
}