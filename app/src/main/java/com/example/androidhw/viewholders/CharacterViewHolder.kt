package com.example.androidhw.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidhw.R
import com.example.androidhw.databinding.ItemCharacterBinding
import com.example.androidhw.repositories.MyUiModel

class CharacterViewHolder(
    private val binding: ItemCharacterBinding,
    private val glide: RequestManager,
    private val onDeleteClick: ((Int) -> Unit)?
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.run {
            btnDelete.setOnClickListener {
                onDeleteClick?.invoke(adapterPosition)
            }
        }
    }

    fun onBind(item: MyUiModel.Character) {
        binding.run {
            tvHeadline.text = item.headline
            tvSupportingText.text = item.supportingText
            glide
                .load(item.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.tools_avatar)
                .error(R.drawable.tools_avatar)
                .into(ivAvatar)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            glide: RequestManager,
            onDeleteClick: ((Int) -> Unit)?
        ): CharacterViewHolder = CharacterViewHolder(
            binding = ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            glide = glide,
            onDeleteClick = onDeleteClick
        )
    }
}