package com.dimasla4ee.playlistmaker.feature.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dimasla4ee.playlistmaker.app.ui.theme.PlaylistMakerTheme
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.feature.search.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiState by searchViewModel.uiState.collectAsState()

                PlaylistMakerTheme {
                    SearchPane(
                        uiState = uiState,
                        onQueryChanged = searchViewModel::onQueryChanged,
                        onSearchClicked = searchViewModel::onSearchClicked,
                        onClearQueueClicked = searchViewModel::onClearQueueClicked,
                        onClearSearchHistoryClicked = searchViewModel::onClearSearchHistoryClicked,
                        onRetryClicked = searchViewModel::onRetryClicked,
                        onTrackClicked = { track ->
                            searchViewModel.onTrackClicked(track)
                            navigateToPlayer(track)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        searchViewModel.onPause()
    }

    private fun navigateToPlayer(track: Track) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
        )
    }

}
