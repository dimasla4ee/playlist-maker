package com.dimasla4ee.playlistmaker.feature.player.presentation

import android.os.Bundle
import android.view.View
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
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.presentation.util.show
import com.dimasla4ee.playlistmaker.core.presentation.util.tintedDrawable
import com.dimasla4ee.playlistmaker.core.presentation.util.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentPlayerBinding
import com.dimasla4ee.playlistmaker.feature.player.presentation.bottom_sheet_adapter.PlaylistBottomSheetAdapter
import com.dimasla4ee.playlistmaker.feature.player.presentation.model.PlaylistAddTrackState
import com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel.MediaPlayerViewModel
import com.dimasla4ee.playlistmaker.feature.search.presentation.mapper.TrackDetailedInfoMapper
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.TrackDetailedInfo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private val binding by viewBinding(FragmentPlayerBinding::bind)
    private lateinit var trackDetailedInfo: TrackDetailedInfo
    private val args: PlayerFragmentArgs by navArgs()
    private val mediaPlayerViewModel: MediaPlayerViewModel by viewModel {
        parametersOf(trackDetailedInfo.audioUrl)
    }
    private val trackPlayerViewModel: TrackPlayerViewModel by viewModel {
        parametersOf(args.track)
    }
    private val bottomSheetAdapter = PlaylistBottomSheetAdapter { playlist ->
        trackPlayerViewModel.onPlaylistClicked(playlist)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackDetailedInfo = TrackDetailedInfoMapper.map(args.track)
        fillTrackInfo(trackDetailedInfo)

        trackPlayerViewModel.onViewCreated()

        setupBottomSheet()
        setupListeners()
        setupObservers()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerViewModel.onPause()
    }

    private fun setupBottomSheet() {
        binding.playlistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.playlistsRecyclerView.adapter = bottomSheetAdapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.visibility =
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) View.GONE else View.VISIBLE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            trackPlayerViewModel.onShowPlaylists()
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

        newPlaylistButton.setOnClickListener {
            findNavController().navigate(PlayerFragmentDirections.actionPlayerFragmentToNewPlaylistFragment())
        }
    }

    private fun setupObservers(): Unit = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            trackPlayerViewModel.isFavorite.collect { isFavorite ->
                val (resId, colorRes) = if (isFavorite) {
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

                val iconRes = if (mediaPlayerState is MediaPlayerViewModel.State.Playing) {
                    R.drawable.ic_pause_24
                } else {
                    R.drawable.ic_play_24
                }
                playButton.setIconResource(iconRes)

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
                val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun fillTrackInfo(track: TrackDetailedInfo): Unit = with(binding) {
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
            val radius = resources.getDimension(R.dimen.coverCornerRadius)
            val placeholder = context.tintedDrawable(
                R.drawable.ic_placeholder_45,
                R.color.coverPlaceholder
            )

            songCover.load(track.coverUrl) {
                placeholder(placeholder)
                error(placeholder)
                transformations(RoundedCornersTransformation(radius))
                crossfade(true)
            }
        }
    }

}