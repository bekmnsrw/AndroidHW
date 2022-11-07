package com.example.androidhw

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context?,
        intent: Intent?
    ) {
        intent?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, SecondActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        NotificationProvider(context!!).createNotification(
            "WAKE UP!",
            "Wake up or you'll be late!",
            pendingIntent
        )
    }
}