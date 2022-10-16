package com.example.androidhw.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.androidhw.CustomItemDecorator
import com.example.androidhw.R
import com.example.androidhw.SeriesAdapter
import com.example.androidhw.databinding.FragmentSeriesBinding
import com.example.androidhw.repositories.SeriesRepository
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter

class SeriesFragment : Fragment(R.layout.fragment_series) {
    private var binding: FragmentSeriesBinding? = null
    private var adapter: SeriesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSeriesBinding.bind(view)

        binding?.run {
            val itemDecoration = CustomItemDecorator(requireContext(), 8.0F)

            adapter = SeriesAdapter(SeriesRepository.series, Glide.with(this@SeriesFragment)) {
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        androidx.appcompat.R.anim.abc_slide_in_top,
                        androidx.appcompat.R.anim.abc_fade_out,
                        androidx.appcompat.R.anim.abc_fade_in,
                        androidx.appcompat.R.anim.abc_fade_out)
                    .replace(R.id.container, DetailedInformationFragment.createBundle(it))
                    .addToBackStack(null)
                    .commit()
            }

            rvSeries.adapter = adapter

            rvSeries.addItemDecoration(itemDecoration)
            rvSeries.adapter = ScaleInAnimationAdapter(adapter!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}