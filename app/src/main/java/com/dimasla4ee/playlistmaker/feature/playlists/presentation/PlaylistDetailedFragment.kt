package com.dimasla4ee.playlistmaker.feature.playlists.presentation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.utils.show
import com.dimasla4ee.playlistmaker.core.utils.tintedDrawable
import com.dimasla4ee.playlistmaker.core.utils.toMm
import com.dimasla4ee.playlistmaker.core.utils.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentPlaylistDetailedBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.moreButton.setOnClickListener {
            showDeleteDialog()
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
    }

    private fun showDeleteDialog() {
        viewModel.playlist.value?.name ?: ""
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.exit_confirmation_title)) //TODO
            .setMessage(getString(R.string.exit_confirmation_message))
            .setNegativeButton(R.string.dismiss) { _, _ -> }
            .setPositiveButton(R.string.done) { _, _ ->
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
}
