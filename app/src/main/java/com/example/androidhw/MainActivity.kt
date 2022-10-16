package com.example.androidhw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidhw.databinding.ActivityMainBinding
import com.example.androidhw.fragments.SeriesFragment

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        binding?.run {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right)
                .replace(R.id.container, SeriesFragment())
                .commit()
        }
    }
}