package de.datlag.musicslide.util

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager

class ReceiverUtil {

    companion object {
        fun Context.register(
            receiver: BroadcastReceiver,
            filter: IntentFilter,
            keep: Boolean = false
        ) {
            this.unregister(receiver)
            this.registerReceiver(receiver, filter)
            if (keep)
                notKillReceiver(this, receiver::class.java)
        }

        fun Context.unregister(receiver: BroadcastReceiver) {
            try {
                this.unregisterReceiver(receiver)
            } catch (ignored: Exception) { }
            killReceiver(this, receiver::class.java)
        }

        private fun killReceiver(context: Context, clazz: Class<out BroadcastReceiver>) {
            val runningReceiver = ComponentName(context.applicationContext.packageName, clazz.name)
            context.packageManager.setComponentEnabledSetting(
                runningReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        private fun notKillReceiver(context: Context, clazz: Class<out BroadcastReceiver>) {
            val newReceiver = ComponentName(context.applicationContext.packageName, clazz.name)
            if (context.packageManager.getComponentEnabledSetting(newReceiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
                context.packageManager.setComponentEnabledSetting(
                    newReceiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
        }
    }

}