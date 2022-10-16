package com.example.androidhw

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidhw.databinding.ItemSeriesBinding
import com.example.androidhw.repositories.Series

class SeriesAdapter(
    private val list: List<Series>,
    private val glide: RequestManager,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<SeriesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeriesViewHolder = SeriesViewHolder(ItemSeriesBinding.inflate(
        LayoutInflater.from(parent.context), parent, false),
        glide,
        onItemClick
    )

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) = holder.onBind(list[position])

    override fun getItemCount(): Int = list.size
}