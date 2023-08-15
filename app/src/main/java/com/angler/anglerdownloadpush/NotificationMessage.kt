package com.angler.anglerdownloadpush.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NotificationMessage : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Perform your service functionality here
        // For example, you can show a notification or perform a background task

        // For demonstration, let's show a simple log message
        println("NotificationMessage Service started")

        return super.onStartCommand(intent, flags, startId)
    }
}
