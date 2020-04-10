package de.datlag.musicslide

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.widget.Toast
import de.datlag.musicslide.receiver.BootReceiver

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setBootOptions()
        Toast.makeText(this, "Receiver registered", Toast.LENGTH_LONG).show()
    }

    private fun setBootOptions() {
        registerReceiver(BootReceiver(), IntentFilter(Intent.ACTION_BOOT_COMPLETED))

        val onBootReceive = ComponentName(applicationContext.packageName, BootReceiver::class.java.name)
        if (packageManager.getComponentEnabledSetting(onBootReceive) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
            packageManager.setComponentEnabledSetting(onBootReceive, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
    }

}