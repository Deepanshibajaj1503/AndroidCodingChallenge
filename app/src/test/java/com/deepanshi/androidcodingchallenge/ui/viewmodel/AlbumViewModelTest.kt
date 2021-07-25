package com.deepanshi.androidcodingchallenge.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.deepanshi.androidcodingchallenge.data.api.ApiHelper
import com.deepanshi.androidcodingchallenge.data.model.Album
import com.deepanshi.androidcodingchallenge.data.repository.MainRepository
import com.deepanshi.androidcodingchallenge.data.room.AlbumDao
import com.deepanshi.androidcodingchallenge.ui.RxImmediateSchedulerRule
import com.deepanshi.androidcodingchallenge.ui.TestCoroutineRule
import com.deepanshi.androidcodingchallenge.utils.Resource
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class AlbumViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var apiHelper: ApiHelper

    @Mock
    private lateinit var apiUsersObserver: Observer<Resource<List<Album>>>

    @Mock
    private lateinit var dao: AlbumDao

    private val validAlbumList = listOf(Album(1, 2, "abc"), Album(1, 3, "def"), Album(1, 4, "xyz"))

    @Before
    fun setUp() {
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {

            `when`(apiHelper.getAlbums()).thenReturn(Single.just(emptyList<Album>()))

            val repository = MainRepository(apiHelper, dao)
            val viewModel = AlbumViewModel(repository)
            viewModel.fetchAlbumsFromServer()
            viewModel.getAlbums().observeForever(apiUsersObserver)
            verify(apiHelper).getAlbums()
            verify(apiUsersObserver).onChanged(Resource.success(emptyList<Album>()))
            viewModel.getAlbums().removeObserver(apiUsersObserver)

        }
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            val throwable = Throwable("Exception")
            `when`(apiHelper.getAlbums()).thenReturn(Single.error(throwable))

            val repository = MainRepository(apiHelper, dao)
            val viewModel = AlbumViewModel(repository)
            viewModel.fetchAlbumsFromServer()
            viewModel.getAlbums().observeForever(apiUsersObserver)
            verify(apiHelper).getAlbums()
            verify(apiUsersObserver).onChanged(
                Resource.error(data = null, message = throwable.message.toString())
            )
            viewModel.getAlbums().removeObserver(apiUsersObserver)
        }
    }

    @After
    fun tearDown() {
    }
}