package de.datlag.musicslide.util

import android.content.Context
import android.content.IntentFilter
import de.datlag.musicslide.R
import de.datlag.musicslide.receiver.SpotifyReceiver
import de.datlag.musicslide.util.ReceiverUtil.Companion.register
import de.datlag.musicslide.util.ReceiverUtil.Companion.unregister

class StreamingUtil {

    companion object {

        private val spotifyReceiver = SpotifyReceiver()

        fun registerReceiver(context: Context) {
            val activeFilter = context.getString(R.string.spotify_package)+context.getString(R.string.spotify_active)
            val playbackFilter = context.getString(R.string.spotify_package)+context.getString(R.string.spotify_playback_change)
            val queueFilter = context.getString(R.string.spotify_package)+context.getString(R.string.spotify_queue_change)
            val metaFilter = context.getString(R.string.spotify_package)+context.getString(R.string.spotify_meta_change)

            val spotifyFilter = IntentFilter(activeFilter)
            spotifyFilter.addAction(playbackFilter)
            spotifyFilter.addAction(queueFilter)
            spotifyFilter.addAction(metaFilter)

            context.register(spotifyReceiver, spotifyFilter, true)
        }

        fun unregisterReceiver(context: Context) {
            context.unregister(spotifyReceiver)
        }
    }

}