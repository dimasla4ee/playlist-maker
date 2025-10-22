package com.dimasla4ee.playlistmaker.core.data.network

import com.dimasla4ee.playlistmaker.core.domain.model.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}