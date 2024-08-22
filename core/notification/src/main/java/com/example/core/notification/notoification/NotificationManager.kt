package com.example.core.notification.notoification

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.core.notification.R

interface NotificationManager {
    fun showNotification(
        title: String,
        smallIcon: Int,
        description: String = "00:00:00",
        pendingIntent: PendingIntent? = null,
        startService: ((Notification) -> Unit)? = null
    )
}


class NotificationManagerImpl(
    private val context: Context
) : NotificationManager {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

    private var baseNotification: NotificationCompat.Builder? = null

    init {
        createNotificationChannel()
    }

    override fun showNotification(
        title: String,
        smallIcon: Int,
        description: String,
        pendingIntent: PendingIntent?,
        startService: ((Notification) -> Unit)?
    ) {

        if (baseNotification == null) {
            baseNotification = NotificationCompat.Builder(context, RUN_TRACKING_CHANNEL_ID)
                .setSmallIcon(smallIcon)
                .setContentText(description)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
        }
        baseNotification?.let {
            val notification = it
                .setContentText(description)
                .build()
            startService?.invoke(notification)
            notificationManager.notify(1, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                RUN_TRACKING_CHANNEL_ID,
                context.getString(R.string.runners),
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }


    private companion object {
        const val RUN_TRACKING_CHANNEL_ID = "runt_tracking"
    }
}