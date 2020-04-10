package de.datlag.musicslide

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import de.datlag.musicslide.receiver.BootReceiver
import de.datlag.musicslide.services.LockService

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startServices()
        setBootOptions()
        Toast.makeText(this, "Receiver and Service registered", Toast.LENGTH_LONG).show()
    }

    private fun startServices() {
        registerReceiver(BootReceiver(), IntentFilter(Intent.ACTION_BOOT_COMPLETED))
        ContextCompat.startForegroundService(this, Intent(this, LockService::class.java))
        startService(Intent(this, LockService::class.java))
    }

    private fun setBootOptions() {
        val onBootReceive = ComponentName(applicationContext.packageName, BootReceiver::class.java.name)
        if (packageManager.getComponentEnabledSetting(onBootReceive) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
            packageManager.setComponentEnabledSetting(onBootReceive, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
    }

}