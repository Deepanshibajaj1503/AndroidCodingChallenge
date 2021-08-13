package com.deepanshi.androidcodingchallenge.data.api

import com.deepanshi.androidcodingchallenge.data.model.Album
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("albums")
    fun getAlbums(): Single<List<Album>>

    @GET("albums/{album_id}")
    suspend fun fetchAlbumDetail(@Path("album_id") albumId: Int): Album
}

