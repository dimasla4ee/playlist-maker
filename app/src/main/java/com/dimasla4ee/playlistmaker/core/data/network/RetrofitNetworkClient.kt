package com.dimasla4ee.playlistmaker.core.data.network

import com.dimasla4ee.playlistmaker.core.domain.model.Response
import com.dimasla4ee.playlistmaker.core.util.LogUtil
import com.dimasla4ee.playlistmaker.feature.search.data.model.TrackSearchRequest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitNetworkClient : NetworkClient {

    private const val BASE_URL = "https://itunes.apple.com/"
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .build()
    }

    private val service: ItunesService by lazy {
        retrofit.create(ItunesService::class.java)
    }

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