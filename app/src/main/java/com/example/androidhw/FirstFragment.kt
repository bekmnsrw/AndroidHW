package com.example.androidhw

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.androidhw.databinding.FragmentFirstBinding

class FirstFragment : Fragment(R.layout.fragment_first) {
    private var binding: FragmentFirstBinding? = null

    private var counter: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)

        parentFragmentManager.popBackStack("SecondFragment", 0)

        binding?.tvCounter?.text = "Counter value: $counter"

        binding?.run {
            btnNavigateToSecondFragment.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                    .replace(R.id.container, SecondFragment.createBundle(counter))
                    .addToBackStack("SecondFragment")
                    .commit()
            }

            btnIncreaseCounter.setOnClickListener {
                counter++
                saveCounter(counter)
                tvCounter.text = "Counter value: $counter"
            }

            btnOpenDialogFragment.setOnClickListener {
                val dialog = CustomDialogFragment(counterValue = counter) {
                    counter = it
                    saveCounter(it)
                    tvCounter.text = "Counter value: $counter"

                }
                dialog.show(parentFragmentManager, "DialogFragment")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val SAVED_COUNTER = "saved_counter"
    }

    private fun saveCounter(counter: Long) {
        arguments?.apply {
            putLong(SAVED_COUNTER, counter)
        }
    }
}