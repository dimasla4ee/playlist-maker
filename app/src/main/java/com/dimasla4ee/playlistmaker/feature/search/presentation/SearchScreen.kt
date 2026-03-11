@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)

package com.dimasla4ee.playlistmaker.feature.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.app.ui.theme.AppDimensions
import com.dimasla4ee.playlistmaker.app.ui.theme.AppTypography
import com.dimasla4ee.playlistmaker.app.ui.theme.appSearchBarColors
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.presentation.components.ActionButton
import com.dimasla4ee.playlistmaker.core.presentation.components.StateInfo
import com.dimasla4ee.playlistmaker.core.presentation.components.TitleAppBar
import com.dimasla4ee.playlistmaker.core.presentation.components.TracksContent
import com.dimasla4ee.playlistmaker.core.utils.fadingEdge
import com.dimasla4ee.playlistmaker.feature.search.presentation.model.SearchUiState

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onClearQueueClicked: () -> Unit,
    onClearSearchHistoryClicked: () -> Unit,
    onRetryClicked: () -> Unit,
    onTrackClicked: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val searchBarColors = appSearchBarColors()
    val inputField = remember {
        @Composable {
            SearchBarDefaults.InputField(
                textStyle = AppTypography.bodyLarge,
                textFieldState = textFieldState,
                searchBarState = searchBarState,
                onSearch = {
                    onSearchClicked()
                },
                placeholder = {
                    Text(
                        style = AppTypography.bodyLarge,
                        modifier = Modifier.clearAndSetSemantics {},
                        text = stringResource(R.string.searchbar_hint)
                    )
                },
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(AppDimensions.searchBarIconSize),
                        painter = painterResource(R.drawable.ic_search_24),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                textFieldState.clearText()
                                onClearQueueClicked()
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(AppDimensions.searchBarIconSize),
                                painter = painterResource(R.drawable.ic_clear_24),
                                contentDescription = stringResource(R.string.clear)
                            )
                        }
                    }
                },
                colors = appSearchBarColors().inputFieldColors
            )
        }
    }

    LaunchedEffect(textFieldState.text) {
        onQueryChanged(textFieldState.text.toString())
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TitleAppBar(
                title = stringResource(R.string.search),
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimensions.paddingMedium),
                state = searchBarState,
                inputField = inputField,
                colors = searchBarColors
            )
            ExpandedFullScreenSearchBar(
                state = searchBarState,
                inputField = inputField,
                colors = searchBarColors
            ) {
                when (uiState) {
                    is SearchUiState.Error -> ErrorContent(onRetryClicked = onRetryClicked)
                    is SearchUiState.NoResults -> EmptyResultsContent()
                    is SearchUiState.Loading -> LoadingContent()
                    is SearchUiState.Content -> TracksContent(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = AppDimensions.paddingMedium)
                            .fadingEdge(),
                        onTrackClicked = onTrackClicked,
                        tracks = uiState.results
                    )

                    is SearchUiState.History -> SearchHistoryResults(
                        onClearSearchHistoryClicked = onClearSearchHistoryClicked,
                        onTrackClicked = onTrackClicked,
                        tracks = uiState.history
                    )

                    is SearchUiState.Idle -> {}
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StateInfo(
            text = stringResource(R.string.network_error),
            drawable = R.drawable.ic_no_internet_120,
        )
        Spacer(modifier.height(AppDimensions.paddingBig))
        ActionButton(
            onClick = onRetryClicked,
            text = stringResource(R.string.retry)
        )
    }
}

@Composable
private fun EmptyResultsContent(
    modifier: Modifier = Modifier
) {
    StateInfo(
        text = stringResource(R.string.no_results),
        drawable = R.drawable.ic_nothing_found_120,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator(modifier.size(AppDimensions.loadingIndicatorSize))
    }
}

@Composable
private fun SearchHistoryResults(
    tracks: List<Track>,
    onTrackClicked: (Track) -> Unit,
    onClearSearchHistoryClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = AppDimensions.paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 24.dp),
            text = stringResource(R.string.been_searched),
            style = AppTypography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        TracksContent(
            tracks = tracks,
            onTrackClicked = onTrackClicked,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = AppDimensions.paddingMedium)
                .fadingEdge()
        )
        ActionButton(
            onClick = onClearSearchHistoryClicked,
            text = stringResource(R.string.clear_history)
        )
    }
}
