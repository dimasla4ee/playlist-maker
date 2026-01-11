package com.dimasla4ee.playlistmaker.feature.playlists.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
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
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.adapter.TrackAdapter
import com.dimasla4ee.playlistmaker.core.utils.debounce
import com.dimasla4ee.playlistmaker.core.utils.show
import com.dimasla4ee.playlistmaker.core.utils.tintedDrawable
import com.dimasla4ee.playlistmaker.core.utils.toMm
import com.dimasla4ee.playlistmaker.core.utils.toMmSs
import com.dimasla4ee.playlistmaker.core.utils.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentPlaylistDetailedBinding
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.model.PlaylistDetailedUiState
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchPlaylistData()
        setupMenuBottomSheet()

        binding.appBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.moreButton.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

        binding.deleteMenuButton.setOnClickListener {
            showDeletePlaylistDialog()
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.editMenuButton.setOnClickListener {
            findNavController().navigate(
                PlaylistDetailedFragmentDirections.actionPlaylistDetailedFragmentToNewPlaylistFragment(
                    playlistId = args.playlistId
                )
            )
        }

        binding.shareButton.setOnClickListener {
            onShareClick()
        }

        binding.shareMenuButton.setOnClickListener {
            onShareClick()
        }
    }

    private fun onShareClick() {
        val playlist = viewModel.playlist.value ?: return
        if (playlist.tracks.isEmpty()) {
            Toast.makeText(requireContext(), R.string.no_tracks_to_share, Toast.LENGTH_SHORT)
                .show()
        } else {
            val message = buildString {
                appendLine(playlist.name)
                if (!playlist.description.isNullOrEmpty()) {
                    appendLine(playlist.description)
                }
                appendLine(
                    resources.getQuantityString(
                        R.plurals.tracks_number,
                        playlist.tracks.size,
                        playlist.tracks.size
                    )
                )

                playlist.tracks.forEachIndexed { index, track ->
                    append("${index + 1}. ${track.artist} - ${track.title} (${track.duration.toMmSs()})")
                    if (index < playlist.tracks.size - 1) {
                        appendLine()
                    }
                }
            }

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, message)
                type = getString(R.string.plain_text_intent_type)
            }

            startActivity(Intent.createChooser(shareIntent, null))
        }
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

    private fun setupMenuBottomSheet() {
        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        menuBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.overlay.show(newState != BottomSheetBehavior.STATE_HIDDEN)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun onItemClick(track: Track) {
        onTrackClickedDebounce(track)
    }

    private fun showDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track_dialog_message))
            .setNegativeButton(R.string.no) { _, _ -> }
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.onDeleteTrack(track)
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

        val radius = resources.getDimension(R.dimen.thumbnailCornerRadius)
        binding.playlistItem.playlistName.text = playlist.name
        binding.playlistItem.playlistTrackCount.text = tracksText
        binding.playlistItem.playlistCover.load(playlist.coverPath) {
            placeholder(placeholder)
            error(placeholder)
            transformations(RoundedCornersTransformation(radius))
            crossfade(true)
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
