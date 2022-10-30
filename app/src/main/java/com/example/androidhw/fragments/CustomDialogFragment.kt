package com.example.androidhw.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.androidhw.repositories.MyUiModel
import com.example.androidhw.R
import com.example.androidhw.repositories.Repository
import com.example.androidhw.databinding.FragmentDialogBinding


class CustomDialogFragment(
    private val onFabClick: (position: Int, MyUiModel.Character) -> Unit
): DialogFragment(R.layout.fragment_dialog) {

    private var binding: FragmentDialogBinding? = null

    override fun onStart() {
        super.onStart()
        val metrics: DisplayMetrics = resources.displayMetrics
        val width: Int = metrics.widthPixels
        dialog?.window?.setLayout(
            width,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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

        binding?.run {
            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }

            btnAdd.setOnClickListener {
                val headline = etHeadline.text.toString()
                val supportingText = etSupportingText.text.toString()

                if (headline.isBlank()) {
                    tilHeadline.error = "Headline can't be null!"
                } else if (supportingText.isBlank()) {
                    tilHeadline.isErrorEnabled = false
                    tilSupportingText.error = "Supporting text can't be null!"
                } else {
                    tilSupportingText.isErrorEnabled = false
                    val position =
                        if (etPosition.text.isNotBlank()) {
                            Integer.valueOf(etPosition.text.toString())
                        } else {
                            Repository.dataList.size
                        }

                    onFabClick(
                        position,
                        MyUiModel.Character(
                            headline,
                            supportingText,
                            ""
                        )
                    )

                    dialog?.hide()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}