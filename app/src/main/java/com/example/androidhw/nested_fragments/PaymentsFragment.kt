package com.example.androidhw.nested_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidhw.R
import com.example.androidhw.databinding.FragmentPaymentsBinding

class PaymentsFragment : Fragment(R.layout.fragment_payments) {
    private var binding: FragmentPaymentsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPaymentsBinding.bind(view)

        binding?.run {
            btnNavigateToChooseRecipientFragment.setOnClickListener {
                findNavController().navigate(R.id.action_paymentsFragment_to_chooseRecipientFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}