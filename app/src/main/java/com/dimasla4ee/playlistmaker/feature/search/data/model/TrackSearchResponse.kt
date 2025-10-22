package com.dimasla4ee.playlistmaker.feature.search.data.model

import com.dimasla4ee.playlistmaker.core.domain.model.Response
import kotlinx.serialization.Serializable

@Serializable
data class TrackSearchResponse(
    val results: List<TrackDto>
) : Response()