package com.dimasla4ee.playlistmaker.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.util.dpToPx
import com.dimasla4ee.playlistmaker.core.presentation.util.tintedDrawable
import com.dimasla4ee.playlistmaker.databinding.TrackItemBinding
import com.dimasla4ee.playlistmaker.feature.search.presentation.mapper.TrackDetailedInfoMapper

class TrackViewHolder(
    private val binding: TrackItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val context = itemView.context

    fun bind(
        track: Track,
        onItemClick: (Track) -> Unit
    ) {
        val trackDetailedInfo = TrackDetailedInfoMapper.map(track)
        val dpRadius = itemView.resources.getDimension(R.dimen.small_25)
        val pxRadius = dpRadius.dpToPx(context).toInt()
        val placeholder = context.tintedDrawable(
            R.drawable.ic_placeholder_45,
            R.color.coverPlaceholder
        )

        with(binding) {
            titleLabel.text = track.title
            artistDurationLabel.text = context.getString(
                R.string.artist_and_time,
                track.artist, trackDetailedInfo.duration
            )

            trackContainer.setOnClickListener {
                onItemClick(track)
            }

            Glide.with(itemView)
                .load(track.thumbnailUrl)
                .placeholder(placeholder)
                .transform(RoundedCorners(pxRadius))
                .fitCenter()
                .into(albumCover)
        }
    }

    companion object {

        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackItemBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }
}