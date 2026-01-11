package com.dimasla4ee.playlistmaker.feature.playlists.presentation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.adapter.TrackAdapter
import com.dimasla4ee.playlistmaker.core.utils.debounce
import com.dimasla4ee.playlistmaker.core.utils.show
import com.dimasla4ee.playlistmaker.core.utils.tintedDrawable
import com.dimasla4ee.playlistmaker.core.utils.toMm
import com.dimasla4ee.playlistmaker.core.utils.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentPlaylistDetailedBinding
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.model.PlaylistDetailedUiState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailedFragment : Fragment(R.layout.fragment_playlist_detailed) {

    private val binding by viewBinding(FragmentPlaylistDetailedBinding::bind)
    private val args: PlaylistDetailedFragmentArgs by navArgs()
    private val viewModel: PlaylistDetailedViewModel by viewModel {
        parametersOf(args.playlistId)
    }
    private lateinit var recyclerAdapter: TrackAdapter
    private lateinit var onTrackClickedDebounce: (Track) -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.moreButton.setOnClickListener {
            showDeletePlaylistDialog()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlist.collect { playlist ->
                playlist?.let { renderPlaylistInfo(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.closeScreen.collect {
                findNavController().popBackStack()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    render(state)
                }
            }
        }

        onTrackClickedDebounce = debounce(
            delayMillis = 300L,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true
        ) { track ->
            findNavController().navigate(
                PlaylistDetailedFragmentDirections.actionPlaylistDetailedFragmentToPlayerFragment(
                    track
                )
            )
        }

        recyclerAdapter = TrackAdapter(
            onItemClick = { onItemClick(it) },
            onLongItemClick = { showDeleteTrackDialog(it) }
        )
        binding.playlistsRecyclerView.adapter = recyclerAdapter
    }

    private fun onItemClick(track: Track) {
        onTrackClickedDebounce(track)
    }

    private fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.delete_track_dialog_message))
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.onDeleteTrack(track)
            }
            .show()
    }

    private fun showDeletePlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(
                getString(
                    R.string.delete_playlist_dialog_message,
                    binding.playlistTitle.text
                )
            )
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.onDeletePlaylist()
            }
            .show()
    }

    private fun renderPlaylistInfo(playlist: Playlist) {
        binding.playlistTitle.text = playlist.name

        if (playlist.description.isNullOrEmpty()) {
            binding.playlistDescription.show(false)
        } else {
            binding.playlistDescription.show(true)
            binding.playlistDescription.text = playlist.description
        }

        val totalMinutes = playlist.duration.toMm()
        val minutesText = resources.getQuantityString(
            R.plurals.minutes_number, totalMinutes.toInt(), totalMinutes.toInt()
        )
        val tracksText = resources.getQuantityString(
            R.plurals.tracks_number, playlist.trackCount, playlist.trackCount
        )

        binding.playlistDurationAndTracklist.text = getString(
            R.string.playlist_duration_and_tracklist,
            minutesText, tracksText
        )

        val placeholder = requireContext().tintedDrawable(
            R.drawable.ic_placeholder_45,
            R.color.coverPlaceholder
        )

        binding.playlistCover.load(playlist.coverPath) {
            placeholder(placeholder)
            error(placeholder)
            crossfade(true)
            binding.playlistCover.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    private fun render(uiState: PlaylistDetailedUiState) = with(binding) {
        when (uiState) {
            is PlaylistDetailedUiState.Content -> {
                recyclerAdapter.submitList(uiState.tracks)
                playlistsRecyclerView.show(true)
                stateInfo.show(false)
            }

            PlaylistDetailedUiState.Empty -> {
                recyclerAdapter.submitList(emptyList())
                playlistsRecyclerView.show(false)
                stateInfo.show(true)
            }

            PlaylistDetailedUiState.Loading -> {
                playlistsRecyclerView.show(false)
                stateInfo.show(false)
            }
        }
    }

}
