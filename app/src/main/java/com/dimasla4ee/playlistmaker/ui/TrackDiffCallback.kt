package com.dimasla4ee.playlistmaker.ui

import androidx.recyclerview.widget.DiffUtil
import com.dimasla4ee.playlistmaker.domain.model.Track

class TrackDiffCallback() : DiffUtil.ItemCallback<Track>() {

    override fun areItemsTheSame(
        oldItem: Track,
        newItem: Track
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Track,
        newItem: Track
    ): Boolean = oldItem == newItem
}