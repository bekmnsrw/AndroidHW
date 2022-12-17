package com.example.androidhw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidhw.databinding.ActivityMainBinding
import com.example.androidhw.fragments.DetailedFragment
import com.example.androidhw.fragments.PlaylistFragment

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, PlaylistFragment(), "PLAYLIST_FRAGMENT")
            .commit()

        if (intent.getStringExtra(NotificationProvider.DETAILED_FRAGMENT_INTENT) != null &&
            intent.getStringExtra(NotificationProvider.DETAILED_FRAGMENT_INTENT) == NotificationProvider.DETAILED_FRAGMENT) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, DetailedFragment.createBundle(intent.getIntExtra(NotificationProvider.TRACK_ID, 0)))
                .addToBackStack("PLAYLIST_FRAGMENT")
                .commit()
        }
    }
}
