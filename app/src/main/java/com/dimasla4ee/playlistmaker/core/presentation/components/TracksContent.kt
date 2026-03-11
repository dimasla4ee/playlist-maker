package com.dimasla4ee.playlistmaker.core.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dimasla4ee.playlistmaker.core.domain.model.Track
import com.dimasla4ee.playlistmaker.core.utils.toMmSs

@Composable
fun TracksContent(
    tracks: List<Track>,
    onTrackClicked: (Track) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = tracks,
            key = { track -> track.id }
        ) { track ->
            TrackItem(
                title = track.title,
                author = track.artist,
                duration = track.duration.toMmSs(),
                imageUrl = track.thumbnailUrl,
                onClick = { onTrackClicked(track) }
            )
        }
    }
}
