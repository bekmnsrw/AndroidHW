package com.example.androidhw.fragments

import android.app.Service
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.widget.SeekBar
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.androidhw.R
import com.example.androidhw.TrackService
import com.example.androidhw.databinding.FragmentDetailedBinding
import com.example.androidhw.model.Track
import com.example.androidhw.model.TrackRepository

class DetailedFragment : Fragment(R.layout.fragment_detailed) {

    private var binding: FragmentDetailedBinding? = null
    private var binder: TrackService.TrackBinder? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = service as? TrackService.TrackBinder
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            binder = null
        }
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val currentTrackId: Int = intent.getIntExtra(PlaylistFragment.CURRENT_TRACK_ID, 0)
                val mediaActions: String? = intent.getStringExtra(PlaylistFragment.MEDIA_ACTION)

                if (mediaActions != null) {
                    if (mediaActions == "PLAY") {
                        initView(currentTrackId, TrackService.ICON_PAUSE)
                    } else {
                        initView(currentTrackId, TrackService.ICON_PLAY)
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, IntentFilter(PlaylistFragment.UPDATE_UI))
    }

    override fun onDetach() {
        super.onDetach()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().bindService(
            Intent(requireActivity(), TrackService::class.java),
            connection,
            Service.BIND_AUTO_CREATE
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailedBinding.bind(view)

        val track: Track = TrackRepository.tracks.single { it.id == arguments?.getInt(ARG_ID) }

        if (track.isPlaying) {
            initView(track.id, TrackService.ICON_PAUSE)
        } else {
            initView(track.id, TrackService.ICON_PLAY)
        }
    }

    private fun initView(id: Int, icon: Int) {
        val track: Track = TrackRepository.tracks.single { it.id == id }

        binding?.run {
            ivCover.setImageResource(track.cover)
            tvName.text = track.name
            tvAuthor.text = track.author

            btnPlay.setImageResource(icon)

            if (icon == TrackService.ICON_PLAY) {
                btnPlay.setOnClickListener {
                    binder?.playTrack(track.id)
                }
            } else {
                btnPlay.setOnClickListener {
                    binder?.pauseTrack()
                }
            }

            btnStop.setOnClickListener {
                binder?.stopTrack()
            }

            btnNext.setOnClickListener {
                binder?.nextTrack()
            }

            btnPrevious.setOnClickListener {
                binder?.previousTrack()
            }

            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        binder?.seek(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val ARG_ID = "ARG_ID"

        fun createBundle(id: Int) = DetailedFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_ID, id)
            }
        }
    }
}
