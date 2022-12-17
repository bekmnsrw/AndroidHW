package com.example.androidhw

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.androidhw.fragments.PlaylistFragment
import com.example.androidhw.model.TrackRepository

class TrackService : Service() {

    private var mediaPlayer = MediaPlayer()
    private var mediaPlayerCurrentPosition: Int = 0
    private var currentTrackId: Int = 0
    private var previousTrackId: Int = -1
    private var notificationProvider: NotificationProvider? = null

    inner class TrackBinder : Binder() {

        fun playTrack(id: Int) {
            play(id)
        }

        fun pauseTrack() {
            pause()
        }

        fun stopTrack() {
            stop()
        }

        fun nextTrack() {
            next()
        }

        fun previousTrack() {
            previous()
        }

        fun seek(position: Int) {
            seekTo(position)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationProvider = NotificationProvider(baseContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            MediaActions.PLAY.name -> play(currentTrackId)
            MediaActions.PAUSE.name -> pause()
            MediaActions.STOP.name -> stop()
            MediaActions.NEXT.name -> next()
            MediaActions.PREVIOUS.name -> previous()
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = TrackBinder()

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun play(id: Int) {
        if (mediaPlayer.isPlaying) {
            mediaPlayerCurrentPosition = 0
            mediaPlayer.stop()
        }

        if (currentTrackId != id) {
            mediaPlayerCurrentPosition = 0
            mediaPlayer.stop()
        }

        currentTrackId = id

        mediaPlayer = MediaPlayer.create(applicationContext, TrackRepository.getTrack(id).raw)
        mediaPlayer.start()
        mediaPlayer.seekTo(mediaPlayerCurrentPosition)
        mediaPlayer.setOnCompletionListener {
            next()
        }

        updateNotification(ICON_PAUSE, id, MediaActions.PLAY, mediaPlayer.duration, mediaPlayerCurrentPosition)
        updateUi(currentTrackId, "PLAY")
    }

    private fun pause() {
        mediaPlayerCurrentPosition = mediaPlayer.currentPosition
        mediaPlayer.stop()
        updateNotification(ICON_PLAY, currentTrackId, MediaActions.PAUSE, mediaPlayer.duration, mediaPlayerCurrentPosition)
        updateUi(currentTrackId, "PAUSE")
    }

    private fun stop() {
        mediaPlayer.stop()
        mediaPlayerCurrentPosition = 0
        updateNotification(ICON_PLAY, currentTrackId, MediaActions.STOP, mediaPlayer.duration, mediaPlayerCurrentPosition)
        updateUi(currentTrackId, "PAUSE")
    }

    private fun next() {
        mediaPlayerCurrentPosition = 0

        previousTrackId = currentTrackId

        if (currentTrackId == -1) {
            return
        }

        currentTrackId = if (currentTrackId < TrackRepository.tracks.size - 1) {
            currentTrackId + 1
        } else {
            0
        }

        updateUi(previousTrackId, currentTrackId)
        play(currentTrackId)
    }

    private fun previous() {
        previousTrackId = currentTrackId

        if (currentTrackId == -1) {
            return
        }

        currentTrackId = if (currentTrackId > 0) {
            currentTrackId - 1
        } else {
            TrackRepository.tracks.size - 1
        }

        updateUi(previousTrackId, currentTrackId)
        play(currentTrackId)
    }

    private fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
    }

    private fun updateUi(previousId: Int, currentId: Int) {
        val intent = Intent(PlaylistFragment.UPDATE_UI)
        intent.putExtra(PlaylistFragment.PREVIOUS_TRACK_ID,previousId)
        intent.putExtra(PlaylistFragment.CURRENT_TRACK_ID, currentId)
        LocalBroadcastManager.getInstance(this@TrackService).sendBroadcast(intent)
    }

    private fun updateUi(currentId: Int, action: String) {
        val intent = Intent(PlaylistFragment.UPDATE_UI)
        intent.putExtra(PlaylistFragment.CURRENT_TRACK_ID, currentId)
        intent.putExtra(PlaylistFragment.MEDIA_ACTION, action)
        LocalBroadcastManager.getInstance(this@TrackService).sendBroadcast(intent)
    }

    private fun updateNotification(@DrawableRes icon: Int, currentTrackId: Int, action: MediaActions, duration: Int, current: Int) {
        val notification = notificationProvider?.createNotification(icon, currentTrackId, action, duration, current)
        startForeground(NotificationProvider.NOTIFICATION_ID, notification)
    }

    companion object {
        const val ICON_PAUSE = R.drawable.ic_pause
        const val ICON_PLAY = R.drawable.ic_play

        val PENDING_INTENT_FLAG =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
        }
    }
}
