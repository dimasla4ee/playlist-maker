package com.dimasla4ee.playlistmaker.feature.media_library.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.app.ui.theme.AppDimensions
import com.dimasla4ee.playlistmaker.core.domain.model.Playlist
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.components.TitleAppBar
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.FavoriteTracksPane
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.model.FavoriteTracksUiState
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.UserPlaylistsPane
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.model.UserPlaylistsUiState
import kotlinx.coroutines.launch

@Composable
fun MediaLibraryPane(
    favoriteTracksUiState: FavoriteTracksUiState,
    userPlaylistsUiState: UserPlaylistsUiState,
    onTrackClicked: (Track) -> Unit,
    onPlaylistClicked: (Playlist) -> Unit,
    onCreatePlaylistClicked: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabs = listOf(
        stringResource(R.string.favorite_tracks),
        stringResource(R.string.playlists)
    )
    val tabIndicatorWidthRatio = 0.4f
    val windowWidthPx = LocalWindowInfo.current.containerSize.width
    val windowWidthDp = with(LocalDensity.current) { windowWidthPx.toDp() }
    val indicatorWidth = windowWidthDp * tabIndicatorWidthRatio

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TitleAppBar(
                title = stringResource(R.string.media_library),
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PrimaryTabRow(
                containerColor = Color.Transparent,
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        color = colorResource(R.color.tabIndicator),
                        modifier = Modifier.tabIndicatorOffset(
                            pagerState.currentPage,
                            matchContentSize = false
                        ),
                        width = indicatorWidth,
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        selectedContentColor = colorResource(R.color.tabIndicator),
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(title) }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> {
                        FavoriteTracksPane(
                            modifier = Modifier.padding(top = AppDimensions.paddingMedium),
                            uiState = favoriteTracksUiState,
                            onTrackClicked = { onTrackClicked(it) }
                        )
                    }

                    1 -> {
                        UserPlaylistsPane(
                            uiState = userPlaylistsUiState,
                            onPlaylistClicked = { onPlaylistClicked(it) },
                            onNewPlaylistClicked = { onCreatePlaylistClicked() }
                        )
                    }
                }
            }
        }
    }
}