package com.dimasla4ee.playlistmaker.feature.playlists.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.app.ui.theme.AppDimensions
import com.dimasla4ee.playlistmaker.app.ui.theme.AppTypography
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.presentation.components.ActionButton
import com.dimasla4ee.playlistmaker.core.presentation.components.StateInfo
import com.dimasla4ee.playlistmaker.core.utils.fadingEdge
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.model.UserPlaylistsUiState

@Composable
fun UserPlaylistsPane(
    uiState: UserPlaylistsUiState,
    onPlaylistClicked: (Playlist) -> Unit,
    onNewPlaylistClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionButton(
            modifier = Modifier.padding(vertical = AppDimensions.paddingMedium),
            onClick = onNewPlaylistClicked,
            text = stringResource(R.string.new_playlist)
        )

        when (uiState) {
            is UserPlaylistsUiState.Content -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .fadingEdge()
                ) {
                    items(
                        items = uiState.playlists,
                        key = { playlist -> playlist.id }
                    ) { playlist ->
                        PlaylistItem(
                            playlist = playlist,
                            onPlaylistClicked = onPlaylistClicked
                        )
                    }
                }
            }

            is UserPlaylistsUiState.Empty -> {
                NoPlaylists(modifier = Modifier.weight(1f))
            }

            is UserPlaylistsUiState.Loading -> {}
        }
    }
}

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onPlaylistClicked: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPlaylistClicked(playlist) }
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        AsyncImage(
            model = playlist.coverPath,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            placeholder = painterResource(R.drawable.ic_placeholder_45),
            error = painterResource(R.drawable.ic_placeholder_45)
        )

        Text(
            text = playlist.name,
            style = AppTypography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = pluralStringResource(
                R.plurals.tracks_number,
                playlist.trackCount,
                playlist.trackCount
            ),
            style = AppTypography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun NoPlaylists(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StateInfo(
            text = stringResource(R.string.no_playlists_yet),
            drawable = R.drawable.ic_nothing_found_120,
        )
    }
}
