package com.example.androidhw

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidhw.repositories.Repository

class DeleteItemOnLeftSwipe(
    private val listAdapter: MyListAdapter?
): ItemTouchHelper(object : SimpleCallback(0, LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) {
        Repository.deleteItem(viewHolder.adapterPosition)
        listAdapter?.submitList(Repository.dataList)
    }

})
