package com.dimasla4ee.playlistmaker.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.utils.tintedDrawable
import com.dimasla4ee.playlistmaker.databinding.TrackItemBinding
import com.dimasla4ee.playlistmaker.feature.search.presentation.mapper.TrackDetailedInfoMapper

class TrackViewHolder(
    private val binding: TrackItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val context = itemView.context

    fun bind(
        track: Track,
        onItemClick: (Track) -> Unit,
        onLongItemClick: ((Track) -> Unit)? = null
    ) {
        val trackDetailedInfo = TrackDetailedInfoMapper.map(track)
        val radius = itemView.resources.getDimension(R.dimen.thumbnailCornerRadius)
        val placeholder = context.tintedDrawable(
            R.drawable.ic_placeholder_45,
            R.color.coverPlaceholder
        )

        with(binding) {
            titleLabel.text = track.title
            artistLabel.text = track.artist
            durationLabel.text = context.getString(
                R.string.track_duration_template,
                trackDetailedInfo.duration
            )

            trackContainer.setOnClickListener {
                onItemClick(track)
            }

            trackContainer.setOnLongClickListener {
                onLongItemClick?.invoke(track)
                true
            }

            albumCover.load(track.thumbnailUrl) {
                placeholder(placeholder)
                error(placeholder)
                transformations(RoundedCornersTransformation(radius))
                crossfade(true)
            }
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
