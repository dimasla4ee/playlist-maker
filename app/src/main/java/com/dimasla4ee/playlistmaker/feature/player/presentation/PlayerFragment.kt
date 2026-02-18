package com.dimasla4ee.playlistmaker.feature.player.presentation

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.dimasla4ee.playlistmaker.BuildConfig
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.utils.show
import com.dimasla4ee.playlistmaker.core.utils.tintedDrawable
import com.dimasla4ee.playlistmaker.core.utils.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentPlayerBinding
import com.dimasla4ee.playlistmaker.feature.player.presentation.adapter.PlaylistBottomSheetAdapter
import com.dimasla4ee.playlistmaker.feature.player.presentation.model.PlaylistAddTrackState
import com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel.MediaPlayerViewModel
import com.dimasla4ee.playlistmaker.feature.search.presentation.mapper.TrackDetailedInfoMapper
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.TrackDetailedInfo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private val binding by viewBinding(FragmentPlayerBinding::bind)
    private lateinit var trackDetailedInfo: TrackDetailedInfo
    private val args: PlayerFragmentArgs by navArgs()
    private val mediaPlayerViewModel: MediaPlayerViewModel by viewModel {
        parametersOf(args.track.audioUrl)
    }
    private val trackPlayerViewModel: TrackPlayerViewModel by viewModel {
        parametersOf(args.track)
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val bottomSheetAdapter = PlaylistBottomSheetAdapter { playlist ->
        trackPlayerViewModel.onPlaylistClicked(playlist)
    }
    private lateinit var analytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackDetailedInfo = TrackDetailedInfoMapper.map(args.track).also { track ->
            fillTrackInfo(track)
        }

        trackPlayerViewModel.onViewCreated()
        setupAnalytics()
        setupBottomSheet()
        setupListeners()
        setupObservers()
    }

    private fun setupAnalytics() {
        analytics = FirebaseAnalytics.getInstance(requireContext())
        analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerViewModel.onPause()
    }

    private fun setupBottomSheet(): Unit = with(binding) {
        playlistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        playlistsRecyclerView.adapter = bottomSheetAdapter
        overlay.isClickable = true

        bottomSheetBehavior = BottomSheetBehavior.from(playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        overlay.show(newState != BottomSheetBehavior.STATE_HIDDEN)
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        overlay.alpha = alphaFromOffset(slideOffset)
                    }

                    /**
                     * Calculates the alpha value based on the bottom sheet's slide offset.
                     *
                     * @param slideOffset The current slide offset of the bottom sheet, where:
                     * - -1.0 is hidden
                     * - 0.0 is collapsed
                     * - 1.0 is expanded
                     * @return A float value between 0.0 and 1.0 representing the transparency, where:
                     * - 0.0 is completely transparent
                     * - 1.0 is completely opaque
                     */
                    private fun alphaFromOffset(slideOffset: Float): Float =
                        (1 + slideOffset).coerceIn(0f, 1f)

                }
            )
        }
    }

    private fun setupListeners(): Unit = with(binding) {
        playButton.setOnClickListener {
            mediaPlayerViewModel.onPlayButtonClicked()
        }

        appBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        addToFavoriteButton.setOnClickListener {
            trackPlayerViewModel.onFavoriteClicked()
        }

        addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            trackPlayerViewModel.onShowPlaylists()
        }

        newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                PlayerFragmentDirections.actionPlayerFragmentToNewPlaylistFragment(playlistId = -1)
            )
        }
    }

    private fun setupObservers(): Unit = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            trackPlayerViewModel.isFavorite.collect { isFavorite ->
                val (resId, colorRes) = if (isFavorite) {
                    analytics.logEvent("add_to_favorite") {
                        param(FirebaseAnalytics.Param.CONTENT, args.track.id.toString())
                    }
                    R.drawable.ic_favorite_active_24 to R.color.favFabActiveIcon
                } else {
                    R.drawable.ic_favorite_inactive_24 to R.color.fabIcon
                }

                addToFavoriteButton.setIconResource(resId)
                addToFavoriteButton.setIconTintResource(colorRes)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mediaPlayerViewModel.state.collect { mediaPlayerState ->
                playButton.isEnabled = mediaPlayerState.isPlayButtonEnabled
                playButton.setPlaying(mediaPlayerState is MediaPlayerViewModel.State.Playing)
                songCurrentDuration.text = mediaPlayerState.progress
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            trackPlayerViewModel.playlists.collect {
                bottomSheetAdapter.playlists = it
                bottomSheetAdapter.notifyDataSetChanged()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            trackPlayerViewModel.playlistAddTrackState.collect { state ->
                val message = when (state) {
                    is PlaylistAddTrackState.Success -> getString(
                        R.string.added_to_playlist,
                        state.playlistName
                    )

                    is PlaylistAddTrackState.AlreadyExists -> getString(
                        R.string.track_already_in_playlist,
                        state.playlistName
                    )
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun fillTrackInfo(track: TrackDetailedInfo): Unit = with(binding) {
        fun LabelValueView.setValueOrHide(value: String?) {
            if (value != null) setValue(value) else show(false)
        }

        songCover.load(track.coverUrl) {
            val radius = resources.getDimension(R.dimen.coverCornerRadius)
            val placeholder = requireContext().tintedDrawable(
                R.drawable.ic_placeholder_45,
                R.color.coverPlaceholder
            )

            placeholder(placeholder)
            error(placeholder)
            transformations(RoundedCornersTransformation(radius))
            crossfade(true)
        }

        songTitle.text = track.title
        songAuthor.text = track.artist

        songDuration.setValue(track.duration)
        songYear.setValueOrHide(track.year?.toString())
        songAlbum.setValueOrHide(track.album)
        songGenre.setValue(track.genre)
        songCountry.setValue(track.country)
    }

}
