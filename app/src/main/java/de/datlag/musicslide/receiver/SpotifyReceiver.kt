package de.datlag.musicslide.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SpotifyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { it ->
            when (it.action) {
                BroadcastTypes.METADATA_CHANGED -> {
                    Log.e("Spotify", "Artist: ${it.getStringExtra("artist")}")
                    Log.e("Spotify", "Album: ${it.getStringExtra("album")}")
                    Log.e("Spotify", "Track: ${it.getStringExtra("track")}")
                    Log.e("Spotify", "Length: ${it.getIntExtra("length", 0)}")
                }
                BroadcastTypes.PLAYBACK_STATE_CHANGED -> {
                    Log.e("Spotify", "State changed")
                }
                BroadcastTypes.QUEUE_CHANGED -> {
                    Log.e("Spotify", "Queue changed")
                }
                BroadcastTypes.ACTIVE -> {
                    Log.e("Spotify", "is active")
                }
                else -> {
                    Log.e("Spotify", "Action: ${it.action}")
                }
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