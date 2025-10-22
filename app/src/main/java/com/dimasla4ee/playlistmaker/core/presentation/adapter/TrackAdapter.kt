package com.dimasla4ee.playlistmaker.core.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.dimasla4ee.playlistmaker.core.domain.model.Track

class TrackAdapter(
    private val onItemClick: (Track) -> Unit
) : ListAdapter<Track, TrackViewHolder>(TrackDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder = TrackViewHolder.from(parent)

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        val track = getItem(position)
        holder.bind(track, onItemClick)
    }
}