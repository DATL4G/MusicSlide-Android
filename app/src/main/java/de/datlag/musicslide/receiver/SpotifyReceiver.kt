package de.datlag.musicslide.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.datlag.musicslide.R
import de.datlag.musicslide.util.SpotifyUtil
import java.util.*

class SpotifyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val activeFilter = context?.getString(R.string.spotify_package)+context?.getString(R.string.active)
        val playbackFilter = context?.getString(R.string.spotify_package)+context?.getString(R.string.playback_state_change)
        val queueFilter = context?.getString(R.string.spotify_package)+context?.getString(R.string.queue_change)
        val metaFilter = context?.getString(R.string.spotify_package)+context?.getString(R.string.meta_changed)

        intent?.let { it ->
            when (it.action) {
                activeFilter -> {
                    SpotifyUtil.lastBeat = Calendar.getInstance()
                }
                playbackFilter -> {
                    SpotifyUtil.lastBeat = Calendar.getInstance()
                }
                queueFilter -> {
                    SpotifyUtil.lastBeat = Calendar.getInstance()
                }
                metaFilter -> {
                    SpotifyUtil.lastBeat = Calendar.getInstance()
                }
                else -> {}
            }
        }
    }

}