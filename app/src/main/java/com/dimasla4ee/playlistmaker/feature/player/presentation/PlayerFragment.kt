package com.dimasla4ee.playlistmaker.feature.player.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.presentation.util.dpToPx
import com.dimasla4ee.playlistmaker.core.presentation.util.show
import com.dimasla4ee.playlistmaker.core.presentation.util.tintedDrawable
import com.dimasla4ee.playlistmaker.core.presentation.util.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentPlayerBinding
import com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel.MediaPlayerViewModel
import com.dimasla4ee.playlistmaker.feature.search.presentation.mapper.TrackDetailedInfoMapper
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.TrackDetailedInfo
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private val binding by viewBinding(FragmentPlayerBinding::bind)
    private lateinit var trackDetailedInfo: TrackDetailedInfo
    private val mediaPlayerViewModel: MediaPlayerViewModel by viewModel {
        parametersOf(trackDetailedInfo.audioUrl)
    }
    private val args: PlayerFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackDetailedInfo = TrackDetailedInfoMapper.map(args.track)
        fillTrackInfo(trackDetailedInfo)

        binding.appBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        with(mediaPlayerViewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                state.collect { mediaPlayerState ->
                    binding.playButton.isEnabled = mediaPlayerState.isPlayButtonEnabled

                    binding.playButton.setIconResource(
                        if (mediaPlayerState is MediaPlayerViewModel.State.Playing) {
                            R.drawable.ic_pause_24
                        } else {
                            R.drawable.ic_play_24
                        }
                    )

                    binding.songCurrentDuration.text = mediaPlayerState.progress
                }
            }

            binding.playButton.setOnClickListener {
                onPlayButtonClicked()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerViewModel.onPause()
    }

    private fun fillTrackInfo(track: TrackDetailedInfo) {
        with(binding) {
            val yearIsAvailable = track.year != null
            val albumIsAvailable = track.album != null

            songDurationFetched.text = track.duration
            songYearFetched.show(yearIsAvailable)
            songYearLabel.show(yearIsAvailable)
            songYearFetched.text = track.year?.toString()

            songAlbumFetched.show(albumIsAvailable)
            songAlbumLabel.show(albumIsAvailable)
            songAlbumFetched.text = track.album

            songGenreFetched.text = track.genre
            songCountryFetched.text = track.country

            songTitle.text = track.title
            songAuthor.text = track.artist

            requireContext().also { context ->
                val dpRadius = resources.getDimension(R.dimen.small_100)
                val pxRadius = dpRadius.dpToPx(context).toInt()
                val placeholder = context.tintedDrawable(
                    R.drawable.ic_placeholder_45,
                    R.color.coverPlaceholder
                )

                AppCompatResources.getDrawable(
                    context, R.drawable.ic_placeholder_45
                )?.apply {
                    setTint(getColor(context, R.color.coverPlaceholder))
                }

                Glide.with(root)
                    .load(track.coverUrl)
                    .placeholder(placeholder)
                    .transform(RoundedCorners(pxRadius))
                    .fitCenter()
                    .into(songCover)
            }
        }
    }
}