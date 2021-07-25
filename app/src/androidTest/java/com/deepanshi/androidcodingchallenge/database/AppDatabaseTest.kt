package com.deepanshi.androidcodingchallenge.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.deepanshi.androidcodingchallenge.data.model.Album
import com.deepanshi.androidcodingchallenge.data.room.AlbumDao
import com.deepanshi.androidcodingchallenge.data.room.AppDatabase
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest : TestCase() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: AlbumDao
    private lateinit var context: Context

    @Before
    public override fun setUp() {
        super.setUp()

        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        dao = db.albumDAO()
    }


    @Test
    fun insertAlbums() = runBlocking {

        val testAlbumList = listOf(Album(1, 2, "abc"), Album(1, 3, "def"), Album(1, 4, "xyz"))

        dao.saveAlbums(testAlbumList)
        val savedAlbums = dao.getAlbums()
        assertThat(savedAlbums.containsAll(testAlbumList)).isTrue()
    }

    @Test
    fun verifyTotalCountOfAlbums() = runBlocking {

        val testAlbumList = listOf(Album(1, 2, "abc"), Album(1, 3, "def"), Album(1, 4, "xyz"))

        dao.saveAlbums(testAlbumList)
        val savedAlbumsCount = dao.getCount()
        assertThat(savedAlbumsCount).isEqualTo(3)
    }



}
