package com.dimasla4ee.playlistmaker.feature.favorite.presentation

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
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.viewmodel.FavoriteViewModel
import com.dimasla4ee.playlistmaker.feature.media_library.presentation.MediaLibraryFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    companion object {
        private val LOG_TAG = FavoriteFragment::class.java.simpleName
        fun newInstance() = FavoriteFragment()
    }

    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by viewModel.uiState.collectAsState()

                PlaylistMakerTheme {
                    FavoritePane(
                        uiState = uiState,
                        onTrackClicked = ::navigateToPlayer
                    )
                }
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

}