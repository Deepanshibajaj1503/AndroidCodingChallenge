package com.deepanshi.androidcodingchallenge.ui.viewmodel

import androidx.lifecycle.*
import com.deepanshi.androidcodingchallenge.data.model.Album
import com.deepanshi.androidcodingchallenge.data.repository.MainRepository
import com.deepanshi.androidcodingchallenge.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlbumViewModel(private val mainRepository: MainRepository) : ViewModel() {

    val albums = MutableLiveData<Resource<List<Album>>>()
    private val compositeDisposable = CompositeDisposable()

    fun getAlbums(): LiveData<Resource<List<Album>>> {
        return albums
    }

    fun fetchAlbumsFromServer() {
        albums.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getAlbums()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ albumList ->
                    onSuccessResponse(albumList)
                }, { throwable ->
                    albums.postValue(
                        Resource.error(
                            data = null,
                            message = throwable.message ?: "Something went wrong!"
                        )
                    )
                })
        )
    }

    private fun onSuccessResponse(albumList: List<Album>?) {
        albumList?.let { list ->
            val sortedList = list.sortedBy { it.title }
            albums.postValue(Resource.success(sortedList))
            saveResponseIntoDB(sortedList)
        }
    }

    private fun saveResponseIntoDB(albumList: List<Album>) {
        viewModelScope.launch {
            mainRepository.saveAlbumsInDB(albumList)
        }
    }


    fun getAlbumsFromLocalDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mainRepository.getCountOfAlbums() > 0) {
                albums.postValue(Resource.success(mainRepository.getAlbumsFromLocalDB()))
            } else {
                albums.postValue(
                    Resource.error(
                        data = null,
                        message = "No Albums to show, Please check your internet connection & try again."
                    )
                )
            }
        }
    }

}