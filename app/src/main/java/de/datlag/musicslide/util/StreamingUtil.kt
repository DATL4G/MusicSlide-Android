package de.datlag.musicslide.util

import android.content.Context
import android.content.IntentFilter
import de.datlag.musicslide.receiver.SpotifyReceiver
import de.datlag.musicslide.util.ReceiverUtil.Companion.register
import de.datlag.musicslide.util.ReceiverUtil.Companion.unregister

class StreamingUtil {

    companion object {

        private val spotifyReceiver = SpotifyReceiver()

        fun registerReceiver(context: Context) {
            val spotifyFilter = IntentFilter("com.spotify.music.active")
            spotifyFilter.addAction("com.spotify.music.playbackstatechanged")
            spotifyFilter.addAction("com.spotify.music.metadatachanged")
            spotifyFilter.addAction("com.spotify.music.queuechanged")

            context.register(spotifyReceiver, spotifyFilter, true)
        }

        fun unregisterReceiver(context: Context) {
            context.unregister(spotifyReceiver)
        }
    }

}