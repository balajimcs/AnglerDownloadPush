package com.angler.anglerdownloadpush

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.*

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Create the notification channel if the Android version is Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        scheduleDailyAlarm()
    }

    private fun scheduleDailyAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Set the alarm to trigger at 5 PM
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 17) // 5 PM
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        // Schedule a repeating alarm
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, // Repeat every day
            pendingIntent
        )
    }

    private fun createNotificationChannel() {
        val channelId = "download_channel_id"
        val channelName = "Download Channel"
        val channelDescription = "Channel for download progress notifications"
        val channelImportance = NotificationManager.IMPORTANCE_LOW // Set the importance level

        val channel = NotificationChannel(channelId, channelName, channelImportance)
        channel.description = channelDescription

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
}
