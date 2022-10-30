package com.example.androidhw

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androidhw.repositories.MyUiModel
import com.example.androidhw.viewholders.AdvertisementViewHolder
import com.example.androidhw.viewholders.CharacterViewHolder
import java.lang.IllegalArgumentException

class MyListAdapter(
    private val glide: RequestManager,
    private val onDeleteClick: ((Int) -> Unit)?
) : ListAdapter<MyUiModel, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<MyUiModel>() {
    override fun areItemsTheSame(
        oldItem: MyUiModel,
        newItem: MyUiModel
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: MyUiModel,
        newItem: MyUiModel
    ): Boolean = oldItem == newItem

}) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_character -> CharacterViewHolder.create(parent, glide, onDeleteClick)
            R.layout.item_advertisement -> AdvertisementViewHolder.create(parent, glide)
            else -> throw IllegalArgumentException("No ViewHolder for such item")
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = currentList[position]
        when (holder) {
            is CharacterViewHolder -> holder.onBind(item as MyUiModel.Character)
            is AdvertisementViewHolder -> holder.onBind(item as MyUiModel.Advertisement)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (currentList[position]) {
            is MyUiModel.Character -> R.layout.item_character
            is MyUiModel.Advertisement -> R.layout.item_advertisement
        }
}