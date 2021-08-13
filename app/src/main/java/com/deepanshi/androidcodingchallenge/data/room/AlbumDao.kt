package com.deepanshi.androidcodingchallenge.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deepanshi.androidcodingchallenge.data.model.Album

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun saveAlbums(entityList: List<Album>)

    @Query("SELECT * FROM Albums")
    fun getAlbums(): List<Album>

    @Query("SELECT COUNT(id) FROM Albums")
    fun getCount(): Int

}