package com.dimasla4ee.playlistmaker.feature.playlists.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist

class PlaylistAdapter(
    private val onPlaylistClickListener: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistItemViewHolder>() {

    var playlists = listOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_item_big, parent, false)
        return PlaylistItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        holder.bind(playlists[position], onPlaylistClickListener)
    }

    override fun getItemCount() = playlists.size
}