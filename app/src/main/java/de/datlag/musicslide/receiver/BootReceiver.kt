package de.datlag.musicslide.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.datlag.musicslide.LockActivity

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if ((it.action == Intent.ACTION_BOOT_COMPLETED || it.action == Intent.ACTION_SCREEN_OFF) && context != null) {
                val mainIntent = Intent(context, LockActivity::class.java)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(mainIntent)
            }
        }
    }

}