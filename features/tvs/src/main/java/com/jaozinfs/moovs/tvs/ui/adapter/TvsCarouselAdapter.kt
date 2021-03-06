package com.jaozinfs.moovs.tvs.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jaozinfs.moovs.extensions.lazyFindView
import com.jaozinfs.moovs.extensions.loadImageCoil
import com.jaozinfs.moovs.tvs.R
import com.jaozinfs.moovs.tvs.data.BASE_BACKDROP_IMAGE_PATTER
import com.jaozinfs.moovs.tvs.domain.model.TvUI


class TvsCarouselAdapter(private val clickListener: (Int) -> Unit) :
    ListAdapter<TvUI, TvsCarouselAdapter.MoviesViewHolder>(
        MoviesDiffUtils
    ) {


    inner class MoviesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageview by view.lazyFindView<ImageView>(R.id.backdrop)
        val title by view.lazyFindView<TextView>(R.id.title)

        fun bind(tvUI: TvUI) {

            view.setOnClickListener { clickListener.invoke(tvUI.id) }

            title.text = tvUI.name
            val uriBg = Uri.parse(BASE_BACKDROP_IMAGE_PATTER)
                .buildUpon()
                .appendEncodedPath(tvUI.poster_path)
                .build()

            imageview.loadImageCoil {
                uri = uriBg
                corners = true
            }
        }
    }

    private object MoviesDiffUtils : DiffUtil.ItemCallback<TvUI>() {
        override fun areItemsTheSame(oldItem: TvUI, newItem: TvUI): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TvUI, newItem: TvUI): Boolean =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {

        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.tvs_item,
                parent,
                false
            )
        )
    }

}

