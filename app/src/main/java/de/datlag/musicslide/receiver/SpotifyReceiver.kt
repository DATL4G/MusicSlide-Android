package de.datlag.musicslide.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.datlag.musicslide.R
import de.datlag.musicslide.util.SpotifyUtil
import java.util.*

class SpotifyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val activeFilter = context?.getString(R.string.spotify_package)+context?.getString(R.string.spotify_active)
        val playbackFilter = context?.getString(R.string.spotify_package)+context?.getString(R.string.spotify_playback_change)
        val queueFilter = context?.getString(R.string.spotify_package)+context?.getString(R.string.spotify_queue_change)
        val metaFilter = context?.getString(R.string.spotify_package)+context?.getString(R.string.spotify_meta_change)

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