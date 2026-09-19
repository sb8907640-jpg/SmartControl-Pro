package com.smartcontrol.owner

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmartControlApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)

            // Default channel
            val defaultChannel = NotificationChannel(
                CHANNEL_DEFAULT,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General app notifications"
            }
            manager.createNotificationChannel(defaultChannel)

            // Alerts channel (high priority)
            val alertsChannel = NotificationChannel(
                CHANNEL_ALERTS,
                "Alerts & SOS",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "SOS, geofence, battery theft alerts"
                enableVibration(true)
            }
            manager.createNotificationChannel(alertsChannel)

            // Consent channel
            val consentChannel = NotificationChannel(
                CHANNEL_CONSENT,
                "Consent Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Consent grant/revoke notifications"
            }
            manager.createNotificationChannel(consentChannel)
        }
    }

    companion object {
        const val CHANNEL_DEFAULT = "smartcontrol_default"
        const val CHANNEL_ALERTS = "smartcontrol_alerts"
        const val CHANNEL_CONSENT = "smartcontrol_consent"
    }
}