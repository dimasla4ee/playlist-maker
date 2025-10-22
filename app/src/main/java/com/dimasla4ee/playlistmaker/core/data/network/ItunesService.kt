package com.dimasla4ee.playlistmaker.core.data.network

import com.dimasla4ee.playlistmaker.feature.search.data.model.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {

    @GET("search")
    fun getSongs(
        @Query("term") query: String,
        @Query("media") mediaType: String = "music"
    ): Call<TrackSearchResponse>
}