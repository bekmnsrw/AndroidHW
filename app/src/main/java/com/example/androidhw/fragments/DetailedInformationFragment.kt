package com.example.androidhw.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.androidhw.R
import com.example.androidhw.databinding.FragmentDetailedInformationBinding
import com.example.androidhw.repositories.Series
import com.example.androidhw.repositories.SeriesRepository

class DetailedInformationFragment : Fragment(R.layout.fragment_detailed_information) {
    private var binding: FragmentDetailedInformationBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDetailedInformationBinding.bind(view)

        val series: Series = SeriesRepository.series.single {
            it.id == arguments?.getInt(ARG_ID)
        }

        binding?.run {
            Glide.with(this@DetailedInformationFragment)
                .load(series.image)
                .placeholder(R.drawable.tools_image)
                .error(R.drawable.tools_image)
                .into(ivImage)

            tvName.text = series.name
            tvGenre.text = "Genre: ${series.genre}"
            tvYear.text = "Year: ${series.year}"
            tvDescription.text = series.description
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val ARG_ID = "ARG_ID"

        fun createBundle(id: Int) = DetailedInformationFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_ID, id)
            }
        }
    }
}