package com.dimasla4ee.playlistmaker.core.data.network

import com.dimasla4ee.playlistmaker.core.domain.model.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}