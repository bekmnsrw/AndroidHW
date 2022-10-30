package com.example.androidhw.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidhw.R
import com.example.androidhw.databinding.ItemAdvertisementBinding
import com.example.androidhw.repositories.MyUiModel

class AdvertisementViewHolder(
    private val binding: ItemAdvertisementBinding,
    private val glide: RequestManager
): RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: MyUiModel.Advertisement) {
        binding.run {
            tvSupportingText.text = item.supportingText
            glide
                .load(item.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.tools_avatar)
                .error(R.drawable.tools_avatar)
                .into(ivImage)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            glide: RequestManager
        ): AdvertisementViewHolder = AdvertisementViewHolder(
            binding = ItemAdvertisementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            glide = glide
        )
    }
}