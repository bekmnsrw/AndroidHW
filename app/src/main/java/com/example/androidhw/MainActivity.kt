package com.example.androidhw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.androidhw.databinding.ActivityMainBinding
import com.example.androidhw.fragments.CustomDialogFragment
import com.example.androidhw.repositories.MyUiModel
import com.example.androidhw.repositories.Repository

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var listAdapter: MyListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        binding?.run {
            listAdapter = MyListAdapter(
                glide = Glide.with(this@MainActivity),
                onDeleteClick = ::onDeleteClick)

            Repository.generateList(15)
            listAdapter?.submitList(Repository.dataList)
            rv.adapter = listAdapter

            DeleteItemOnLeftSwipe(listAdapter = listAdapter).attachToRecyclerView(rv)

            fab.setOnClickListener {
                val dialog = CustomDialogFragment(onFabClick = ::addCharacterItem)
                dialog.show(supportFragmentManager, "Custom Dialog")
            }
        }
    }

    private fun onDeleteClick(position: Int) {
        Repository.deleteItem(position)
        listAdapter?.submitList(Repository.dataList)
    }

    private fun addCharacterItem(position: Int, item: MyUiModel.Character) {
        Repository.addItem(position, item)
        listAdapter?.submitList(Repository.dataList)
    }
}