package com.deepanshi.androidcodingchallenge.data.api


class ApiHelper(private val apiService: ApiService) {

    fun getAlbums() = apiService.getAlbums()

}