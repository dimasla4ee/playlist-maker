package com.dimasla4ee.playlistmaker.feature.favorite.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.components.StateInfo
import com.dimasla4ee.playlistmaker.core.presentation.components.TracksContent
import com.dimasla4ee.playlistmaker.core.utils.fadingEdge
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.model.FavoriteUiState

@Composable
fun FavoritePane(
    uiState: FavoriteUiState,
    onTrackClicked: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is FavoriteUiState.Content -> {
                TracksContent(
                    tracks = uiState.tracks,
                    onTrackClicked = { onTrackClicked(it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .fadingEdge()
                )
            }

            is FavoriteUiState.Empty -> {
                StateInfo(
                    text = stringResource(R.string.empty_media_library),
                    drawable = R.drawable.ic_nothing_found_120,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is FavoriteUiState.Idle -> {}
        }
    }
}
