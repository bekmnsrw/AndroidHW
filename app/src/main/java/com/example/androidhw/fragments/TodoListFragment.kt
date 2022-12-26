package com.example.androidhw.fragments

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.androidhw.MainActivity
import com.example.androidhw.R
import com.example.androidhw.RequestPermissionsProvider
import com.example.androidhw.data.TodoRepository
import com.example.androidhw.databinding.TodoListFragmentBinding
import com.example.androidhw.recycler_view.GridItemDecorator
import com.example.androidhw.recycler_view.TodoListAdapter
import kotlinx.coroutines.launch

class TodoListFragment : Fragment(R.layout.todo_list_fragment) {

    private var binding: TodoListFragmentBinding? = null
    private var adapter: TodoListAdapter? = null
    private var repository: TodoRepository? = null
    private var sharedPreferences: SharedPreferences? = null
    private var requestPermissionsProvider: RequestPermissionsProvider? = null
    private var arePermissionsGranted = false

    private var requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        arePermissionsGranted = it[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                it[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (!arePermissionsGranted) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                requestPermissionsProvider?.suggestUserToGrantPermissions(requireContext())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        sharedPreferences = requireActivity().getSharedPreferences(MainActivity.SHARED_PREF, Context.MODE_PRIVATE)
        requestPermissionsProvider = RequestPermissionsProvider()

        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)

        menu.findItem(R.id.add_todo).isVisible = false

        val icon = if (sharedPreferences?.getInt(MainActivity.THEME, 0) == 0) R.drawable.ic_dark_mode else R.drawable.ic_light_mode
        menu.findItem(R.id.dark_mode).setIcon(icon)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.delete_all -> {
                deleteAllTodo()
                true
            }

            R.id.dark_mode -> {
                changeTheme()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TodoListFragmentBinding.bind(view)

        repository = TodoRepository(requireContext())

        binding?.run {
            val itemDecoration = GridItemDecorator(requireContext(), 16.0F)

            adapter = TodoListAdapter(::onTodoClicked, ::onDeleteClicked)
            rvTodoList.adapter = adapter
            rvTodoList.addItemDecoration(itemDecoration)

            showAllTodo()

            fabCreateTodo.setOnClickListener {
                navigate(-1)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        repository = null
    }

    private fun onTodoClicked(id: Int) {
        navigate(id)
    }

    private fun onDeleteClicked(id: Int) {
        lifecycleScope.launch {
            repository?.deleteTodo(id)
            showAllTodo()
        }
    }

    private fun navigate(id: Int) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            .replace(R.id.container, CreateEditTodoFragment.createBundle(arePermissionsGranted, id))
            .addToBackStack(MainActivity.FRAGMENT_TAG)
            .commit()
    }

    private fun showAllTodo() {
        lifecycleScope.launch {
            val result = repository?.getAllTodo() ?: listOf()
            binding?.tvTextIfEmpty?.visibility = if (result.isEmpty()) View.VISIBLE else View.GONE
            adapter?.submitList(result)
        }
    }

    private fun deleteAllTodo() {
        lifecycleScope.launch {
            repository?.deleteAllTodo()
            showAllTodo()
        }
    }

    private fun changeTheme() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPreferences?.edit()?.putInt(MainActivity.THEME, 1)?.apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPreferences?.edit()?.putInt(MainActivity.THEME, 0)?.apply()
        }
        requireActivity().recreate()
    }
}
