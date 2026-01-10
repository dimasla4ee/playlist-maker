package com.dimasla4ee.playlistmaker.feature.playlists.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.utils.debounce
import com.dimasla4ee.playlistmaker.core.utils.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentPlaylistListBinding
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.MediaLibraryFragmentDirections
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.adapter.PlaylistAdapter
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.model.PlaylistListUiState
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.viewmodel.PlaylistListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistListFragment : Fragment(R.layout.fragment_playlist_list) {

    private val binding by viewBinding(FragmentPlaylistListBinding::bind)
    private val viewModel: PlaylistListViewModel by viewModel()
    private lateinit var onPlaylistClickedDebounce: (Playlist) -> Unit
    private lateinit var recyclerAdapter: PlaylistAdapter

    private fun onItemClick(playlist: Playlist) {
        onPlaylistClickedDebounce(playlist)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onPlaylistClickedDebounce = debounce(
            delayMillis = 300L,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true
        ) { playlist ->
            findNavController().navigate(
                MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlaylistDetailedFragment(
                    playlist.id
                )
            )
        }

        recyclerAdapter = PlaylistAdapter { onItemClick(it) }

        binding.playlistsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistsRecyclerView.adapter = recyclerAdapter

        binding.createPlaylistButton.setOnClickListener {
            findNavController().navigate(
                MediaLibraryFragmentDirections.actionMediaLibraryFragmentToNewPlaylistFragment()
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
    }

    private fun render(state: PlaylistListUiState) {
        when (state) {
            is PlaylistListUiState.Content -> {
                binding.playlistsRecyclerView.isVisible = true
                binding.stateInfo.isVisible = false
                recyclerAdapter.playlists = state.playlists
                recyclerAdapter.notifyDataSetChanged()
            }

            is PlaylistListUiState.Empty -> {
                binding.playlistsRecyclerView.isVisible = false
                binding.stateInfo.isVisible = true
            }

            is PlaylistListUiState.Loading -> {
                binding.playlistsRecyclerView.isVisible = false
                binding.stateInfo.isVisible = false
            }
        }
    }

    companion object {
        fun newInstance() = PlaylistListFragment()
    }

}
