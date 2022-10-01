package com.example.androidhw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.androidhw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEdit.text = "HIDE"

        binding.btnEdit.setOnClickListener {
            with (binding) {
                if (tvUsername.isVisible && imgProfile.isVisible) {
                    tvUsername.visibility = View.GONE
                    imgProfile.visibility = View.GONE
                    btnEdit.text = "SHOW"
                } else {
                    tvUsername.visibility = View.VISIBLE
                    imgProfile.visibility = View.VISIBLE
                    btnEdit.text = "HIDE"
                }
            }
        }

    }
}