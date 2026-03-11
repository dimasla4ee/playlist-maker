package com.dimasla4ee.playlistmaker.feature.media_library.presentation

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
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.viewmodel.FavoriteTracksViewModel
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.viewmodel.UserPlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryFragment : Fragment() {

    private val userPlaylistsViewModel: UserPlaylistsViewModel by viewModel()
    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val favoriteUiState by favoriteTracksViewModel.uiState.collectAsState()
            val userPlaylistUiState by userPlaylistsViewModel.uiState.collectAsState()

            PlaylistMakerTheme {
                MediaLibraryPane(
                    favoriteTracksUiState = favoriteUiState,
                    userPlaylistsUiState = userPlaylistUiState,
                    onTrackClicked = ::navigateToPlayer,
                    onPlaylistClicked = { navigateToPlaylist(it.id) },
                    onCreatePlaylistClicked = ::navigateToPlaylistCreation
                )
            }
        }
    }

    private fun navigateToPlayer(track: Track) {
        findNavController().navigate(
            MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlayerFragment(
                track
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPlaylistsViewModel.getPlaylists()

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
