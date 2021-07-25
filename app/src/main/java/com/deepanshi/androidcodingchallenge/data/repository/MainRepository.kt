package com.deepanshi.androidcodingchallenge.data.repository

import com.deepanshi.androidcodingchallenge.data.api.ApiHelper
import com.deepanshi.androidcodingchallenge.data.model.Album
import com.deepanshi.androidcodingchallenge.data.room.AlbumDao
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainRepository(private val apiHelper: ApiHelper, private val albumDao: AlbumDao) {


    fun getAlbums(): Single<List<Album>> {
        return apiHelper.getAlbums()
    }

    suspend fun saveAlbumsInDB(entityList: List<Album>) {
        withContext(Dispatchers.IO) {
            albumDao.saveAlbums(entityList)
        }
    }

    fun getCountOfAlbums(): Int {
        return albumDao.getCount()
    }

    fun getAlbumsFromLocalDB(): List<Album> {
        return albumDao.getAlbums().sortedBy { it.title }
    }

}