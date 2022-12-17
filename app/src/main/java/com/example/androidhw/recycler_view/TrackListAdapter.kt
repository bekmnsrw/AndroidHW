package com.example.androidhw.recycler_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.androidhw.databinding.ItemTrackBinding
import com.example.androidhw.model.TrackUiModel

class TrackListAdapter(
    private val onItemClick: (Int) -> Unit
): ListAdapter<TrackUiModel, TrackViewHolder>(object : DiffUtil.ItemCallback<TrackUiModel>() {

    override fun areItemsTheSame(
        oldItem: TrackUiModel,
        newItem: TrackUiModel
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TrackUiModel,
        newItem: TrackUiModel
    ): Boolean = oldItem == newItem

    override fun getChangePayload(
        oldItem: TrackUiModel,
        newItem: TrackUiModel
    ): Any? {
        val bundle = Bundle()
        if (oldItem.isPlaying != newItem.isPlaying) {
            bundle.putBoolean(TrackViewHolder.ARG_PLAYING, newItem.isPlaying)
        }
        return if (bundle.isEmpty) super.getChangePayload(oldItem, newItem) else bundle
    }

}) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder = TrackViewHolder(ItemTrackBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    ),
        onItemClick
    )

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        holder.onBind(currentList[position])
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.updateFromBundle(payloads.last() as? Bundle)
        }
    }

}