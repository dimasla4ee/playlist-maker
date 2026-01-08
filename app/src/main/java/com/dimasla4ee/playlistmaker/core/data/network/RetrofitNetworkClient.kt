package com.dimasla4ee.playlistmaker.core.data.network

import com.dimasla4ee.playlistmaker.core.domain.model.Response
import com.dimasla4ee.playlistmaker.core.domain.model.ResultCode
import com.dimasla4ee.playlistmaker.core.utils.LogUtil
import com.dimasla4ee.playlistmaker.feature.search.data.model.TrackSearchRequest
import java.net.UnknownHostException

class RetrofitNetworkClient(
    private val service: ItunesService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response = when (dto) {
        is TrackSearchRequest -> {
            try {
                val body = service.getSongs(dto.term)
                body.apply { resultCode = ResultCode.Ok }
            } catch (e: UnknownHostException) {
                LogUtil.e(LOG_TAG, "doRequest — UnknownHostException: $e")
                Response().apply { resultCode = ResultCode.NoInternet }
            } catch (e: Exception) {
                LogUtil.e(LOG_TAG, "doRequest — Exception: $e")
                Response().apply { resultCode = ResultCode.InternalServerError }
            }
        }

        else -> {
            LogUtil.e(LOG_TAG, "doRequest — unsupported dto")
            Response().apply { resultCode = ResultCode.BadRequest }
        }
    }

    private companion object {
        private const val LOG_TAG = "RetrofitNetworkClient"
    }
}