package com.deepanshi.androidcodingchallenge.ui.view

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.deepanshi.androidcodingchallenge.AndroidCodingChallengeApplication
import com.deepanshi.androidcodingchallenge.R
import com.deepanshi.androidcodingchallenge.data.api.ApiHelper
import com.deepanshi.androidcodingchallenge.data.api.RetrofitBuilder
import com.deepanshi.androidcodingchallenge.data.model.Album
import com.deepanshi.androidcodingchallenge.ui.base.ViewModelFactory
import com.deepanshi.androidcodingchallenge.ui.viewmodel.AlbumViewModel
import com.deepanshi.androidcodingchallenge.utils.NetworkConnectivityUtils
import com.deepanshi.androidcodingchallenge.utils.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_album.*

class AlbumsActivity : AppCompatActivity() {

    private val TAG = AlbumsActivity::class.java.simpleName
    private var viewModel: AlbumViewModel? = null
    private var adapter: AlbumAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        setupViewModel()
        setupUI()
        setupObservers()
        callAlbumsApi()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService), this.application)
        )
            .get(AlbumViewModel::class.java)
    }

    private fun setupUI() {
        adapter = AlbumAdapter(arrayListOf())
        album_rv.adapter = adapter
    }

    /**
     * Observe data
     */
    private fun setupObservers() {
        viewModel?.getAlbums()?.observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        album_rv.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        empty_layout_albums.visibility = View.GONE
                        resource.data?.let { albums -> addListOfAlbums(albums) }
                    }
                    Status.ERROR -> {
                        album_rv.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        empty_layout_albums.visibility = View.VISIBLE
                        showErrorMessage(it.message)
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        album_rv.visibility = View.GONE
                        empty_layout_albums.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun callAlbumsApi() {
        if (AndroidCodingChallengeApplication.applicationContext().isNetworkConnected()) {
            viewModel?.fetchAlbumsFromServer()
        } else {
            viewModel?.getAlbumsFromLocalDatabase()
        }
    }

    private fun addListOfAlbums(albums: List<Album>) {
        adapter?.apply {
            addAlbums(albums)
            notifyDataSetChanged()
        }
    }

    /**
     * handles internet connection and refresh data when internet is connected
     */
    private val mNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            Log.d(TAG, "Network connected!")
            Handler(Looper.getMainLooper()).post {
                viewModel?.fetchAlbumsFromServer()
            }
        }

        override fun onLost(network: Network) {
            Log.d(TAG, "Network Lost!")
            Handler(Looper.getMainLooper()).post {
                viewModel?.getAlbumsFromLocalDatabase()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        NetworkConnectivityUtils.unRegisterConnectivityCallback(mNetworkCallback)
    }

    override fun onStart() {
        super.onStart()
        NetworkConnectivityUtils.registerConnectivityCallback(mNetworkCallback)
    }

    private fun showErrorMessage(message: String?) {
        message?.let {
            Snackbar.make(root_layout as View, it, Snackbar.LENGTH_LONG).show()
        }
    }

}