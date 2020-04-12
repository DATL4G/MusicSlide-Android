package de.datlag.musicslide.services

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import de.datlag.musicslide.util.BootUtil

class LockService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val intentFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED)

        BootUtil.registerReceiver(this, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        BootUtil.unregisterReceiver(this)
    }

}