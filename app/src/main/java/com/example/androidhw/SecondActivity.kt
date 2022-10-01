package com.example.androidhw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidhw.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras?.keySet()?.map { "$it: ${intent.extras?.get(it)}" }?.joinToString { it }
        binding.tvIntentData.text = extras
    }
}