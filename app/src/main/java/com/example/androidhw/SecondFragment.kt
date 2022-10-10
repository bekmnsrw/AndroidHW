package com.example.androidhw

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.androidhw.databinding.FragmentSecondBinding

class SecondFragment : Fragment(R.layout.fragment_second) {
    private var binding: FragmentSecondBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecondBinding.bind(view)

        val counter = arguments?.getLong(ARG_COUNTER)

        binding?.run {
            tvCounter.text = "Counter value: $counter"

            if (counter != null) {
                when (counter) {
                    in 0..50 -> {
                        background.setBackgroundColor(Color.GREEN)
                    }
                    in 51..100 -> {
                        background.setBackgroundColor(Color.GRAY)
                    }
                    else -> {
                        background.setBackgroundColor(Color.YELLOW)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val ARG_COUNTER = "arg_counter"

        fun createBundle(counter: Long) = SecondFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_COUNTER, counter)
            }
        }
    }
}