package com.deepanshi.androidcodingchallenge.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.deepanshi.androidcodingchallenge.AndroidCodingChallengeApplication
import com.deepanshi.androidcodingchallenge.R
import com.deepanshi.androidcodingchallenge.data.api.ApiHelper
import com.deepanshi.androidcodingchallenge.data.api.RetrofitBuilder
import com.deepanshi.androidcodingchallenge.data.model.Album
import com.deepanshi.androidcodingchallenge.ui.base.BaseFragment
import com.deepanshi.androidcodingchallenge.ui.base.ViewModelFactory
import com.deepanshi.androidcodingchallenge.ui.viewmodel.AlbumDetailViewModel
import com.deepanshi.androidcodingchallenge.utils.Status
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_album_detail.*

class AlbumDetailFragment : BaseFragment() {

    private var viewModel: AlbumDetailViewModel? = null
    private var albumId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_album_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initBundle()
        setupObservers()
        setOnClick()
    }

    private fun initBundle() {
        albumId = arguments?.getInt("album_id") ?: 0
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService), requireActivity().application)
        )
            .get(AlbumDetailViewModel::class.java)
    }

    /**
     * Observe data
     */
    private fun setupObservers() {
        if (albumId > 0 && AndroidCodingChallengeApplication.applicationContext().isNetworkConnected()) {
            viewModel?.getAlbumDetail(albumId)?.observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            resource.data?.let { album ->
                                renderUI(album)
                            }
                        }
                        Status.ERROR -> {
                            progressBar.visibility = View.GONE
                            showErrorMessage(it.message)
                        }
                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            })
        } else {
            progressBar.visibility = View.GONE
            showErrorMessage(getString(R.string.common_error))
        }
    }

    private fun renderUI(album: Album) {
        album_title.text = album.title
    }

    private fun setOnClick() {
        back_button.setOnClickListener {
            val navController =
                activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }
            navController?.navigateUp()
        }
    }

    private fun showErrorMessage(message: String?) {
        message?.let {
            Snackbar.make(root_layout as View, it, Snackbar.LENGTH_LONG).show()
        }
    }

}