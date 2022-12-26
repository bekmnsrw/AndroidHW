package com.example.androidhw

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.androidhw.databinding.ActivityMainBinding
import com.example.androidhw.fragments.TodoListFragment

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)

        when (sharedPreferences?.getInt(THEME, 0)) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding?.run {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, TodoListFragment(), FRAGMENT_TAG)
                .commit()
        }
    }

    companion object {
        const val FRAGMENT_TAG = "TODO_LIST_FRAGMENT"
        const val SHARED_PREF = "SHARED_PREF"
        const val THEME = "THEME"
        const val LAST_LONGITUDE = "LAST_LONGITUDE"
        const val LAST_LATITUDE = "LAST_LATITUDE"
        const val SHOULD_REQUEST_LOCATION = "SHOULD_REQUEST_LOCATION"
    }
}
