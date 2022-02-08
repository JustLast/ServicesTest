package com.dorofeev.servicestest

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyIntentService: IntentService(NAME) {

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 10
        private const val NAME = "MyIntentService"

        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
//        setIntentRedelivery(true)
        log("onCreate")
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i")
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text of notification")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyIntentService: $message")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }
}