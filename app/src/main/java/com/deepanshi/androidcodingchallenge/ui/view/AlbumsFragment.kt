package com.deepanshi.androidcodingchallenge.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.deepanshi.androidcodingchallenge.AndroidCodingChallengeApplication
import com.deepanshi.androidcodingchallenge.R
import com.deepanshi.androidcodingchallenge.data.api.ApiHelper
import com.deepanshi.androidcodingchallenge.data.api.RetrofitBuilder
import com.deepanshi.androidcodingchallenge.data.model.Album
import com.deepanshi.androidcodingchallenge.ui.base.BaseFragment
import com.deepanshi.androidcodingchallenge.ui.base.ViewModelFactory
import com.deepanshi.androidcodingchallenge.ui.viewmodel.AlbumViewModel
import com.deepanshi.androidcodingchallenge.utils.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_album.*

class AlbumsFragment : BaseFragment() {

    private val TAG = AlbumsFragment::class.java.simpleName
    private var viewModel: AlbumViewModel? = null
    private var adapter: AlbumAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupUI()
        setupObservers()
        callAlbumsApi()
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService), requireActivity().application)
        )
            .get(AlbumViewModel::class.java)
    }

    private fun setupUI() {
        adapter = AlbumAdapter(arrayListOf()) { albumId ->
            onAlbumClicked(albumId)
        }
        album_rv.adapter = adapter
    }

    private fun onAlbumClicked(albumId: Int) {
        val bundle = Bundle()
        bundle.putInt("album_id", albumId)

        val navController =
            activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }
        navController?.navigate(R.id.action_albumListFragment_to_albumDetailFragment, bundle)
    }

    /**
     * Observe data
     */
    private fun setupObservers() {
        viewModel?.getAlbums()?.observe(viewLifecycleOwner, Observer {
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

    private fun showErrorMessage(message: String?) {
        message?.let {
            Snackbar.make(root_layout as View, it, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onNetworkAvailable() {
        super.onNetworkAvailable()
        Handler(Looper.getMainLooper()).post {
            viewModel?.fetchAlbumsFromServer()
        }
    }

    override fun onNetworkLost() {
        super.onNetworkLost()
        Handler(Looper.getMainLooper()).post {
            viewModel?.getAlbumsFromLocalDatabase()
        }
    }

}