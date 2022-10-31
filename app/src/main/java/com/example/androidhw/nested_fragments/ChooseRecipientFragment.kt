package com.example.androidhw.nested_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidhw.R
import com.example.androidhw.databinding.FragmentChooseRecipientBinding

class ChooseRecipientFragment : Fragment(R.layout.fragment_choose_recipient) {
    private var binding: FragmentChooseRecipientBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChooseRecipientBinding.bind(view)

        binding?.run {
            btnNavigateToChooseAmountFragment.setOnClickListener {
                findNavController().navigate(R.id.action_chooseRecipientFragment_to_chooseAmountFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}