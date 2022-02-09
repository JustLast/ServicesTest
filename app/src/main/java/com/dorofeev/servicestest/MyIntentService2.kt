package com.dorofeev.servicestest

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log

class MyIntentService2: IntentService(NAME) {

    companion object {
        private const val NAME = "MyIntentService2"
        private const val PAGE = "page"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService2::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        setIntentRedelivery(true)
        log("onCreate")
    }

    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        val page = intent?.getIntExtra(PAGE, 0) ?: 0
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyIntentService2: $message")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }
}