package com.dimasla4ee.playlistmaker.feature.player.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.util.dpToPx
import com.dimasla4ee.playlistmaker.core.presentation.util.getParcelableExtraCompat
import com.dimasla4ee.playlistmaker.core.presentation.util.setupWindowInsets
import com.dimasla4ee.playlistmaker.core.presentation.util.show
import com.dimasla4ee.playlistmaker.core.util.Keys
import com.dimasla4ee.playlistmaker.databinding.ActivityPlayerBinding
import com.dimasla4ee.playlistmaker.feature.search.presentation.mapper.TrackDetailedInfoMapper
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.TrackDetailedInfo
import com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel.MediaPlayerViewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var trackDetailedInfo: TrackDetailedInfo
    private val mediaPlayerViewModel: MediaPlayerViewModel by viewModels {
        MediaPlayerViewModel.Companion.getFactory(trackDetailedInfo.audioUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater).apply {
            setContentView(root)
            root.setupWindowInsets()
            enableEdgeToEdge()
        }

        val track = intent.getParcelableExtraCompat<Track>(Keys.TRACK_INFO)
            ?: throw IllegalStateException("Track data is missing in the intent")

        trackDetailedInfo = TrackDetailedInfoMapper.map(track)
        fillTrackInfo(trackDetailedInfo)

        binding.panelHeader.setOnIconClickListener { finish() }

        with(mediaPlayerViewModel) {
            state.observe(this@PlayerActivity) { mediaPlayerState ->
                binding.playButton.isEnabled =
                    mediaPlayerState != MediaPlayerViewModel.Companion.State.DEFAULT

                binding.playButton.setImageResource(
                    if (mediaPlayerState == MediaPlayerViewModel.Companion.State.PLAYING) {
                        R.drawable.ic_pause_24
                    } else {
                        R.drawable.ic_play_24
                    }
                )
            }

            timer.observe(this@PlayerActivity) { timerValue ->
                binding.songCurrentDuration.text = timerValue
            }

            binding.playButton.setOnClickListener {
                onPlayButtonClicked()
            }
        }
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

            val dpRadius = resources.getDimension(R.dimen.small_100)
            val pxRadius = dpRadius.dpToPx(this@PlayerActivity).toInt()
            val placeholder = AppCompatResources.getDrawable(
                this@PlayerActivity, R.drawable.ic_placeholder_45
            )?.apply {
                setTint(getColor(R.color.light_gray))
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