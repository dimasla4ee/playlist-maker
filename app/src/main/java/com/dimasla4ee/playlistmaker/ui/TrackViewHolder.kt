package com.dimasla4ee.playlistmaker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.databinding.TrackItemBinding
import com.dimasla4ee.playlistmaker.domain.model.Track
import com.dimasla4ee.playlistmaker.presentation.mapper.TrackDetailedInfoMapper
import com.dimasla4ee.playlistmaker.presentation.util.dpToPx
import com.dimasla4ee.playlistmaker.util.LogUtil

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
        val placeholder = AppCompatResources.getDrawable(
            context, R.drawable.ic_placeholder_45
        )?.apply {
            setTint(context.getColor(R.color.light_gray))
        }

        with(binding) {
            titleTextView.text = track.title
            artistAndTimeTextView.text = context.getString(
                R.string.artist_and_time,
                track.artist, trackDetailedInfo.duration
            )

            trackContainer.setOnClickListener {
                if (Debouncer.isClickAllowed()) {
                    onItemClick(track)
                    LogUtil.d("TrackViewHolder", "bind: $track")
                }
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