package com.example.androidhw

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.androidhw.databinding.FragmentDialogBinding

class CustomDialogFragment(val counterValue: Long, val onClick: (Long) -> Unit) : DialogFragment(R.layout.fragment_dialog) {
    private var binding: FragmentDialogBinding? = null

    override fun onStart() {
        super.onStart()
        val metrics: DisplayMetrics = resources.displayMetrics
        val width: Int = metrics.widthPixels
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog, container, false).also {
            binding = FragmentDialogBinding.bind(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var inputValue = 0L

        binding?.run {
            btnPositive.setOnClickListener {
                if (isValidNumber()) {
                    inputValue = binding?.textInputLayout?.editText?.text.toString().toLong()
                    onClick(counterValue + inputValue)
                    binding?.textInputLayout?.helperText = "Input is correct! Let's continue"
                }
            }

            btnNegative.setOnClickListener {
                dismiss()
            }

            btnNeutral.setOnClickListener {
                if (isValidNumber()) {
                    inputValue = binding?.textInputLayout?.editText?.text.toString().toLong()
                    if (counterValue - inputValue < 0) {
                        binding?.textInputLayout?.error = "Counter can't be less then 0! Enter a smaller number"
                    } else {
                        onClick(counterValue - inputValue)
                        binding?.textInputLayout?.helperText = "Input is correct! Let's continue"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun isValidNumber(): Boolean {
        val input = binding?.textInputLayout?.editText?.text
        if (!input.isNullOrBlank()) {
            if (input.toString().toLong() !in 0..100) {
                binding?.textInputLayout?.error = "Invalid data format!"
                return false
            } else {
                binding?.textInputLayout?.isErrorEnabled = false
                binding?.textInputLayout?.helperText = " "
            }
        } else {
            binding?.textInputLayout?.error = "Input mustn't be empty!"
            return false
        }
        return true
    }
}