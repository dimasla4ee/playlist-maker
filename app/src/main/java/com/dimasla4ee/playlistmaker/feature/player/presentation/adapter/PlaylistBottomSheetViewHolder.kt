package com.dimasla4ee.playlistmaker.feature.player.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.error
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.utils.tintedDrawable
import com.dimasla4ee.playlistmaker.databinding.PlaylistItemSmallBinding

class PlaylistBottomSheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = PlaylistItemSmallBinding.bind(itemView)
    private val context = itemView.context

    fun bind(playlist: Playlist) {
        binding.playlistName.text = playlist.name
        binding.playlistTrackCount.text = context.resources.getQuantityString(
            R.plurals.tracks_number,
            playlist.trackCount,
            playlist.trackCount
        )

        val radius = context.resources.getDimension(R.dimen.thumbnailCornerRadius)
        val placeholder = context.tintedDrawable(
            R.drawable.ic_placeholder_45,
            R.color.coverPlaceholder
        )

        binding.playlistCover.load(playlist.coverPath) {
            placeholder(placeholder)
            error(placeholder)
            transformations(RoundedCornersTransformation(radius))
        }
    }
}