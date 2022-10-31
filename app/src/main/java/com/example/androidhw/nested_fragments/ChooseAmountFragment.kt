package com.example.androidhw.nested_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidhw.R
import com.example.androidhw.databinding.FragmentChooseAmountBinding

class ChooseAmountFragment : Fragment(R.layout.fragment_choose_amount) {
    private var binding: FragmentChooseAmountBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChooseAmountBinding.bind(view)

        binding?.run {
            btnNavigateToConfirmPaymentFragment.setOnClickListener {
                findNavController().navigate(R.id.action_chooseAmountFragment_to_confirmPaymentFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}