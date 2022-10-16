package com.example.androidhw

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidhw.databinding.ItemSeriesBinding
import com.example.androidhw.repositories.Series

class SeriesViewHolder(
    private val binding: ItemSeriesBinding,
    private val glide: RequestManager,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.run {
            root.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
    }

    fun onBind(series: Series) {
        with(binding) {
            tvName.text = series.name
            tvGenre.text = series.genre

            glide
                .load(series.image)
                .centerCrop()
                .placeholder(R.drawable.tools_image)
                .error(R.drawable.tools_image)
                .into(ivImage)
        }
    }
}