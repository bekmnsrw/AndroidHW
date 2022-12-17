package com.example.androidhw.recycler_view

import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidhw.R
import com.example.androidhw.databinding.ItemTrackBinding
import com.example.androidhw.model.TrackUiModel

class TrackViewHolder(
    private val binding: ItemTrackBinding,
    private val onItemClick: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    private var trackUiModel: TrackUiModel? = null

    init {
        binding.run {
            root.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
    }

    fun onBind(trackUiModel: TrackUiModel) {
        this.trackUiModel = trackUiModel

        with(binding) {
            tvName.text = trackUiModel.name
            tvAuthor.text = trackUiModel.author
            tvGenre.text = trackUiModel.genre
            ivCover.setImageResource(trackUiModel.cover)
            btnPlayPause.setChecked(trackUiModel.isPlaying)
        }
    }

    private fun ImageView.setChecked(isChecked: Boolean) {
        setImageResource(
            if (isChecked) R.drawable.ic_pause else R.drawable.ic_play
        )
    }

    fun updateFromBundle(bundle: Bundle?) {
        if (bundle?.containsKey(ARG_PLAYING) == true) {
            bundle.getBoolean(ARG_PLAYING).also {
                binding.btnPlayPause.setChecked(it)
            }
        }
    }

    companion object {
        const val ARG_PLAYING = "ARG_PLAYING"
    }
}