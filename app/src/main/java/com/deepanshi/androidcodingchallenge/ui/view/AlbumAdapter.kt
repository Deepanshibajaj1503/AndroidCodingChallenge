package com.deepanshi.androidcodingchallenge.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deepanshi.androidcodingchallenge.R
import com.deepanshi.androidcodingchallenge.data.model.Album
import kotlinx.android.synthetic.main.item_album.view.*


class AlbumAdapter(private val albums: ArrayList<Album>) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(album: Album) {
            itemView.apply {
                title.text = album.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder =
        AlbumViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false))

    override fun getItemCount(): Int = albums.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    fun addAlbums(users: List<Album>) {
        this.albums.apply {
            clear()
            addAll(users)
        }

    }

}