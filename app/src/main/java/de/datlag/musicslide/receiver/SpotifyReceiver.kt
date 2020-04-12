package de.datlag.musicslide.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.datlag.musicslide.util.SpotifyUtil
import java.util.*

class SpotifyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { it ->
            when (it.action) {
                BroadcastTypes.METADATA_CHANGED -> {
                    SpotifyUtil.lastBeat = Calendar.getInstance()
                }
                BroadcastTypes.PLAYBACK_STATE_CHANGED -> {
                    SpotifyUtil.lastBeat = Calendar.getInstance()
                }
                BroadcastTypes.QUEUE_CHANGED -> {
                    SpotifyUtil.lastBeat = Calendar.getInstance()
                }
                BroadcastTypes.ACTIVE -> {
                    SpotifyUtil.lastBeat = Calendar.getInstance()
                }
                else -> {}
            }
        }
    }

    companion object {
        internal object BroadcastTypes {
            private const val SPOTIFY_PACKAGE = "com.spotify.music"
            const val PLAYBACK_STATE_CHANGED = "$SPOTIFY_PACKAGE.playbackstatechanged"
            const val QUEUE_CHANGED = "$SPOTIFY_PACKAGE.queuechanged"
            const val METADATA_CHANGED = "$SPOTIFY_PACKAGE.metadatachanged"
            const val ACTIVE = "$SPOTIFY_PACKAGE.active"
        }
    }

}