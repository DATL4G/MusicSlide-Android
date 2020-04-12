package de.datlag.musicslide.util

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import de.datlag.musicslide.receiver.BootReceiver
import de.datlag.musicslide.util.ReceiverUtil.Companion.register
import de.datlag.musicslide.util.ReceiverUtil.Companion.unregister

class BootUtil {

    companion object {

        private val bootReceiver = BootReceiver()

        fun registerReceiver(context: Context, filter: IntentFilter = IntentFilter(Intent.ACTION_BOOT_COMPLETED)) {
            context.register(bootReceiver, filter, true)
        }

        fun unregisterReceiver(context: Context) {
            context.unregister(bootReceiver)
        }

    }

}