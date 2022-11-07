package com.example.androidhw

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationProvider(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val customLightColor = Color.BLUE
    private val customVibrations = arrayOf(500L, 1000L).toLongArray()
    private val customSound = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notification_sound)
    private val customAudioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .build()

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                context.getString(R.string.default_notification_channel_id),
                context.getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lightColor = customLightColor
                vibrationPattern = customVibrations
                setSound(customSound, customAudioAttributes)
            }.also {
                notificationManager.createNotificationChannel(it)
            }
        }
    }

    fun createNotification(title: String, text: String, notificationIntent: PendingIntent) {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(
                context,
                context.getString(R.string.default_notification_channel_id)
            )
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(notificationIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLights(customLightColor, 500, 2000)
                .setVibrate(customVibrations)
                .setSound(customSound)

        notificationManager.notify(1, builder.build())
    }
}