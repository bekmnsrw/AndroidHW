package com.example.androidhw.recycler_view

import androidx.recyclerview.widget.RecyclerView
import com.example.androidhw.data.entities.Todo
import com.example.androidhw.databinding.ItemTodoBinding
import java.text.SimpleDateFormat

class TodoViewHolder(
    private val binding: ItemTodoBinding,
    private val onItemClick: (Int) -> Unit,
    private val onDeleteClicked: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    private var todoModel: Todo? = null

    fun onBind(todoModel: Todo) {
        this.todoModel = todoModel

        with(binding) {
            root.setOnClickListener {
                onItemClick(todoModel.id)
            }

            btnDelete.setOnClickListener {
                onDeleteClicked(todoModel.id)
            }

            tvTitle.text = todoModel.title
            tvDescription.text = todoModel.description
            tvDate.text = if (todoModel.date == null) "Without deadline" else SimpleDateFormat("E, d MMMM yyyy, HH:mm").format(todoModel.date)
        }
    }
}
