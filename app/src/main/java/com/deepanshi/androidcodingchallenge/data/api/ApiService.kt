package com.deepanshi.androidcodingchallenge.data.api

import com.deepanshi.androidcodingchallenge.data.model.Album
import io.reactivex.Single
import retrofit2.http.GET


interface ApiService {

    @GET("albums")
    fun getAlbums(): Single<List<Album>>
}

