package com.dimasla4ee.playlistmaker.feature.playlists.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dimasla4ee.playlistmaker.app.ui.theme.PlaylistMakerTheme
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.MediaLibraryFragmentDirections
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.viewmodel.UserPlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserPlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = UserPlaylistsFragment()
    }

    private val viewModel: UserPlaylistsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val uiState by viewModel.uiState.collectAsState()

            PlaylistMakerTheme {
                UserPlaylistsPane(
                    uiState = uiState,
                    onPlaylistClicked = { navigateToPlaylist(it.id) },
                    onNewPlaylistClicked = { navigateToPlaylistCreation() }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPlaylists()
    }

    private fun navigateToPlaylist(playlistId: Int) {
        findNavController().navigate(
            MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlaylistDetailedFragment(
                playlistId
            )
        )
    }

    private fun navigateToPlaylistCreation() {
        findNavController().navigate(
            MediaLibraryFragmentDirections.actionMediaLibraryFragmentToNewPlaylistFragment()
        )
    }
}
