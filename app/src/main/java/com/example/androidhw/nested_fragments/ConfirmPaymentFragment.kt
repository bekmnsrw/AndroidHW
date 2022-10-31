package com.example.androidhw.nested_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidhw.R
import com.example.androidhw.databinding.FragmentConfirmPaymentBinding

class ConfirmPaymentFragment : Fragment(R.layout.fragment_confirm_payment) {
    private var binding: FragmentConfirmPaymentBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentConfirmPaymentBinding.bind(view)

        binding?.run {
            btnNavigateToPaymentsFragment.setOnClickListener {
                findNavController().navigate(R.id.action_confirmPaymentFragment_to_paymentsFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}