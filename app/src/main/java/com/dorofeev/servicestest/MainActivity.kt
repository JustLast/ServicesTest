package com.dorofeev.servicestest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.dorofeev.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var page = 0

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.simpleService.setOnClickListener {
            startService(MyService.newIntent(this, 25))
        }
        binding.foregroundService.setOnClickListener {
//            showNotification()
            ContextCompat.startForegroundService(
                this, MyForegroundService.newIntent(this)
            )
        }
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(
                this, MyIntentService.newIntent(this)
            )
        }
        // This is for running an enqueue of job services
        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)
            val jobInfo = JobInfo.Builder(MyJobService.JOB_SERVICE_ID, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent= MyJobService.newIntent(page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                val intent = MyIntentService2.newIntent(this, page++)
                startService(intent)
            }
        }

        // This is for a one-time launch a job service (without enqueue)
//        binding.jobScheduler.setOnClickListener {
//            val componentName = ComponentName(this, MyJobService::class.java)
//            val jobInfo = JobInfo.Builder(MyJobService.JOB_SERVICE_ID, componentName)
//                .setExtras(MyJobService.newBundle(page++))
//                .setRequiresCharging(true)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//                .setPersisted(true)
//                .build()
//            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
//            jobScheduler.schedule(jobInfo)
//        }

        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }

        binding.workManager.setOnClickListener {
            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                MyWorker.WORK_NAME,
                ExistingWorkPolicy.APPEND,
                MyWorker.makeRequest(page++)
            )
        }

        binding.stopService.setOnClickListener {
//            stopService(MyForegroundService.newIntent(this))
            stopService(MyIntentService.newIntent(this))
        }
    }

//    private fun showNotification() {
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle("Title")
//            .setContentText("Text of notification")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .build()
//
//        notificationManager.notify(1, notification)
//    }
}