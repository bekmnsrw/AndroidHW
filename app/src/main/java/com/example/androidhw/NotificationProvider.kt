package com.example.androidhw

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadata
import android.os.Build
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.example.androidhw.model.TrackRepository

class NotificationProvider(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val mediaSession: MediaSessionCompat = MediaSessionCompat(context, MEDIA_SESSION_TAG)

    private var previousPendingIntent: PendingIntent? = createPendingIntent(MediaActions.PREVIOUS)
    private var nextPendingIntent: PendingIntent? = createPendingIntent(MediaActions.NEXT)
    private var playPendingIntent: PendingIntent? = createPendingIntent(MediaActions.PLAY)
    private var pausePendingIntent: PendingIntent? = createPendingIntent(MediaActions.PAUSE)
    private var stopPendingIntent: PendingIntent? = createPendingIntent(MediaActions.STOP)

    private fun createPendingIntent(action: MediaActions) = PendingIntent.getService(
        context, REQUEST_CODE,
        Intent(context, TrackService::class.java).setAction(
            action.name
        ), TrackService.PENDING_INTENT_FLAG
    )

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).also {
                notificationManager.createNotificationChannel(it)
            }
        }
    }

    fun createNotification(
        @DrawableRes iconPlayPause: Int,
        id: Int,
        action: MediaActions,
        duration: Int,
        current: Int
    ): Notification {
        createNotificationChannel()

        val notificationPendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            Intent(context, MainActivity::class.java).also {
                it.putExtra(DETAILED_FRAGMENT_INTENT, DETAILED_FRAGMENT)
                it.putExtra(TRACK_ID, id)
            },
            TrackService.PENDING_INTENT_FLAG
        )

        val track = TrackRepository.getTrack(id)

        val playPausePendingIntent = if (action == MediaActions.PLAY) pausePendingIntent else playPendingIntent

        val builder = NotificationCompat.Builder(
            context,
            NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_music)
            .setContentTitle(track.name)
            .setContentText(track.author)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setContentIntent(notificationPendingIntent)
            .addAction(R.drawable.ic_previous, PREVIOUS, previousPendingIntent)
            .addAction(iconPlayPause, PLAY, playPausePendingIntent)
            .addAction(R.drawable.ic_next, NEXT, nextPendingIntent)
            .addAction(R.drawable.ic_stop, STOP, stopPendingIntent)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSession.sessionToken))
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, track.cover))
            .setProgress(duration, current, false)

        mediaSession
            .setMetadata(
                MediaMetadataCompat.Builder()
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration.toLong())
                    .build()
            )

        mediaSession
            .setPlaybackState(
            PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_PAUSE
                            or PlaybackStateCompat.ACTION_SEEK_TO
                            or PlaybackStateCompat.ACTION_STOP
                            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
                .setState(
                    if (action == MediaActions.PLAY) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                    current.toLong(),
                    1f,
                    SystemClock.elapsedRealtime()
                )
                .build()
        )

        notificationManager.notify(NOTIFICATION_ID, builder.build())

        return builder.build()
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "CHANNEL_ID"
        private const val NOTIFICATION_CHANNEL_NAME = "CHANNEL_NAME"
        private const val REQUEST_CODE = 123
        private const val MEDIA_SESSION_TAG = "MEDIA_SESSION_TAG"
        private const val PREVIOUS = "PREVIOUS"
        private const val PLAY = "PLAY"
        private const val NEXT = "NEXT"
        private const val STOP = "STOP"
        const val NOTIFICATION_ID = 321
        const val DETAILED_FRAGMENT_INTENT = "DETAILED_FRAGMENT_INTENT"
        const val DETAILED_FRAGMENT = "DETAILED_FRAGMENT"
        const val TRACK_ID = "TRACK_ID"
    }
}
