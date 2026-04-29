package com.github.person20020.nap.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.github.person20020.nap.Nap

class Timer : Service() {
    private val prefs by lazy {
        (application as Nap).repository
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "service started", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }

    override fun onBind(p0: Intent?): IBinder? = null
}
