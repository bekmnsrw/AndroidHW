package com.example.androidhw.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.androidhw.data.entities.Todo
import com.example.androidhw.databinding.ItemTodoBinding

class TodoListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
): ListAdapter<Todo, TodoViewHolder>(object : DiffUtil.ItemCallback<Todo>() {

    override fun areItemsTheSame(
        oldItem: Todo,
        newItem: Todo
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Todo,
        newItem: Todo
    ): Boolean = oldItem == newItem

}) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoViewHolder = TodoViewHolder(ItemTodoBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    ),
        onItemClick,
        onDeleteClick
    )

    override fun onBindViewHolder(
        holder: TodoViewHolder,
        position: Int
    ) {
        holder.onBind(currentList[position])
    }
}
