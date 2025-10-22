package com.dimasla4ee.playlistmaker.core.data.network

import com.dimasla4ee.playlistmaker.core.domain.model.Response
import com.dimasla4ee.playlistmaker.core.util.LogUtil
import com.dimasla4ee.playlistmaker.feature.search.data.model.TrackSearchRequest

class RetrofitNetworkClient(
    private val service: ItunesService
) : NetworkClient {

    override fun doRequest(dto: Any): Response = when (dto) {
        is TrackSearchRequest -> {
            val response = runCatching {
                service.getSongs(dto.term).execute()
            }.onFailure {
                LogUtil.e("RetrofitNetworkClient", "doRequest: $it")
            }.getOrNull()

            val body = response?.body() ?: Response()
            body.apply { resultCode = response?.code() ?: 0 }
        }

        else -> {
            Response().apply { resultCode = 400 }
        }
    }
}