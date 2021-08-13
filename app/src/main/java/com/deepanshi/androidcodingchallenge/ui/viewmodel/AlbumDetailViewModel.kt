package com.deepanshi.androidcodingchallenge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.deepanshi.androidcodingchallenge.data.repository.MainRepository
import com.deepanshi.androidcodingchallenge.utils.Resource
import kotlinx.coroutines.Dispatchers

class AlbumDetailViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getAlbumDetail(albumId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getAlbumDetail(albumId)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}