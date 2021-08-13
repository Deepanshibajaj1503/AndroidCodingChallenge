package com.deepanshi.androidcodingchallenge.ui.base

import android.app.Application
import com.deepanshi.androidcodingchallenge.data.api.ApiHelper
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.deepanshi.androidcodingchallenge.data.repository.MainRepository
import com.deepanshi.androidcodingchallenge.data.room.AppDatabase
import com.deepanshi.androidcodingchallenge.ui.viewmodel.AlbumDetailViewModel
import com.deepanshi.androidcodingchallenge.ui.viewmodel.AlbumViewModel

class ViewModelFactory(private val apiHelper: ApiHelper,private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
            return AlbumViewModel(MainRepository(apiHelper,AppDatabase.getInstance(application).albumDAO())) as T
        }
        else if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
            return AlbumDetailViewModel(MainRepository(apiHelper,AppDatabase.getInstance(application).albumDAO())) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}