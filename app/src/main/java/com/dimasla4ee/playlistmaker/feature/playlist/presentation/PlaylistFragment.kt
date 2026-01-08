package com.dimasla4ee.playlistmaker.feature.playlist.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.utils.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentPlaylistBinding
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.MediaLibraryFragmentDirections
import com.dimasla4ee.playlistmaker.feature.playlist.presentation.adapter.PlaylistAdapter
import com.dimasla4ee.playlistmaker.feature.playlist.presentation.model.PlaylistsState
import com.dimasla4ee.playlistmaker.feature.playlist.presentation.viewmodel.PlaylistsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    private val binding by viewBinding(FragmentPlaylistBinding::bind)
    private val viewModel: PlaylistsViewModel by viewModel()
    private val adapter = PlaylistAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.playlistsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsRecyclerView.adapter = adapter

        binding.createPlaylistButton.setOnClickListener {
            findNavController().navigate(
                MediaLibraryFragmentDirections.actionMediaLibraryFragmentToNewPlaylistFragment()
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                render(it)
            }
        }
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Content -> {
                binding.playlistsRecyclerView.isVisible = true
                binding.stateInfo.isVisible = false
                adapter.playlists = state.playlists
                adapter.notifyDataSetChanged()
            }

            is PlaylistsState.Empty -> {
                binding.playlistsRecyclerView.isVisible = false
                binding.stateInfo.isVisible = true
            }

            is PlaylistsState.Loading -> {
                binding.playlistsRecyclerView.isVisible = false
                binding.stateInfo.isVisible = false
            }
        }
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}