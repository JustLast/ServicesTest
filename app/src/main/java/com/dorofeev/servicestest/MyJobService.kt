package com.dorofeev.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.*

class MyJobService: JobService() {

    private val scope = CoroutineScope(Dispatchers.Main)

    companion object {
        const val JOB_SERVICE_ID = 5
        private const val PAGE = "page"

        // This is for running an enqueue of job services
        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }

        // This for one-time launch job service
//        fun newBundle(page: Int): PersistableBundle {
//            return PersistableBundle().apply {
//                putInt(PAGE, page)
//            }
//        }
    }

    // This is for running an enqueue of job services
    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            scope.launch {
                var workItem = params?.dequeueWork()
                while (workItem != null) {
                    val page = workItem.intent.getIntExtra(PAGE, 0)

                    for (i in 0 until 5) {
                        delay(1000)
                        log("Timer $i $page")
                    }

                    params?.completeWork(workItem)
                    workItem = params?.dequeueWork()
                }
                jobFinished(params, false)
            }
        }

        // True - Service is still working. False - Service is not working, he finished
        return true
    }

    // This for one-time launch job service
//    override fun onStartJob(params: JobParameters?): Boolean {
//        log("onStartJob")
//        val page= params?.extras?.getInt(PAGE) ?: 0
//        scope.launch {
//            for (i in 0 until 5) {
//                delay(1000)
//                log("Timer $i $page")
//            }
//            jobFinished(params, true)
//        }
//
//        // True - Service is still working. False - Service is not working, he finished
//        return true
//    }

    // This method starts working only if the android system killed this service
    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobService: $message")
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        log("onDestroy")
    }
}