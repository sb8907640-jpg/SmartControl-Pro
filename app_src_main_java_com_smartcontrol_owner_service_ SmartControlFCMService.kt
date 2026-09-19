package com.smartcontrol.owner.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.smartcontrol.owner.MainActivity
import com.smartcontrol.owner.SmartControlApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SmartControlFCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data
        val title = message.notification?.title ?: data["title"] ?: "SmartControl Pro"
        val body = message.notification?.body ?: data["body"] ?: ""

        val channelId = when (data["type"]) {
            "sos", "alert", "geofence", "battery_theft" -> SmartControlApp.CHANNEL_ALERTS
            "consent" -> SmartControlApp.CHANNEL_CONSENT
            else -> SmartControlApp.CHANNEL_DEFAULT
        }

        showNotification(title, body, channelId, data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // TODO: Send token to backend
    }

    private fun showNotification(
        title: String,
        body: String,
        channelId: String,
        data: Map<String, String>
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            data.forEach { (key, value) -> putExtra(key, value) }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(
                if (channelId == SmartControlApp.CHANNEL_ALERTS)
                    NotificationCompat.PRIORITY_HIGH
                else NotificationCompat.PRIORITY_DEFAULT
            )
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}