package com.example.androidhw.fragments

import android.app.Service
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.androidhw.R
import com.example.androidhw.TrackService
import com.example.androidhw.databinding.FragmentPlaylistBinding
import com.example.androidhw.model.TrackRepository
import com.example.androidhw.recycler_view.TrackListAdapter

class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    private var binding: FragmentPlaylistBinding? = null
    private var adapter: TrackListAdapter? = null
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
                val previousTrackId: Int = intent.getIntExtra(PREVIOUS_TRACK_ID, 0)
                val currentTrackId: Int = intent.getIntExtra(CURRENT_TRACK_ID, 0)
                val mediaActions: String? = intent.getStringExtra(MEDIA_ACTION)

                if (mediaActions != null) {
                    redrawPlaying(currentTrackId, mediaActions)
                } else {
                    redraw(previousTrackId, currentTrackId)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, IntentFilter(UPDATE_UI))
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
        requireActivity().startService(Intent(requireActivity(), TrackService::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlaylistBinding.bind(view)

        binding?.run {
            adapter = TrackListAdapter {
                onTrackClicked(it)
                redrawButtons(it)
            }
            rvPlaylist.adapter = adapter
            adapter?.submitList(TrackRepository.tracksUi)
        }
    }

    private fun redrawButtons(currentTrack: Int) {
        var counter = 0

        while (counter != TrackRepository.tracks.size) {
            val track = TrackRepository.tracks[counter]

            if (track.id != currentTrack) {
                val newItem = track.copy(isPlaying = false)
                TrackRepository.tracks.removeAt(track.id)
                TrackRepository.tracks.add(track.id, newItem)
            }

            counter++
        }

        adapter?.submitList(TrackRepository.tracksUi)
    }

    private fun onTrackClicked(trackId: Int) {
        val track = TrackRepository.tracks[trackId]
        val newItem = track.copy(isPlaying = !track.isPlaying)

        TrackRepository.tracks.removeAt(trackId)
        TrackRepository.tracks.add(trackId, newItem)

        adapter?.submitList(TrackRepository.tracksUi)

        if (newItem.isPlaying) {
            binder?.playTrack(newItem.id)
        } else {
            binder?.pauseTrack()
        }
    }

    private fun redraw(previousTrackId: Int, currentTrackId: Int) {
        val previousTrack = TrackRepository.tracks[previousTrackId]
        val currentTrack = TrackRepository.tracks[currentTrackId]
        val newPreviousTrack = previousTrack.copy(isPlaying = false)
        val newCurrentTrack = currentTrack.copy(isPlaying = true)

        TrackRepository.tracks.removeAt(previousTrackId)
        TrackRepository.tracks.add(previousTrackId, newPreviousTrack)

        TrackRepository.tracks.removeAt(currentTrackId)
        TrackRepository.tracks.add(currentTrackId, newCurrentTrack)

        adapter?.submitList(TrackRepository.tracksUi)
    }

    private fun redrawPlaying(currentTrackId: Int, action: String) {
        val currentTrack = TrackRepository.tracks[currentTrackId]

        if (action == "PLAY") {
            val newCurrentTrack = currentTrack.copy(isPlaying = true)
            TrackRepository.tracks.removeAt(currentTrackId)
            TrackRepository.tracks.add(currentTrackId, newCurrentTrack)
        } else {
            val newCurrentTrack = currentTrack.copy(isPlaying = false)
            TrackRepository.tracks.removeAt(currentTrackId)
            TrackRepository.tracks.add(currentTrackId, newCurrentTrack)
        }

        adapter?.submitList(TrackRepository.tracksUi)
    }

    companion object {
        const val PREVIOUS_TRACK_ID = "PREVIOUS_TRACK_ID"
        const val CURRENT_TRACK_ID = "CURRENT_TRACK_ID"
        const val UPDATE_UI = "UPDATE_UI"
        const val MEDIA_ACTION = "MEDIA_ACTION"
    }
}