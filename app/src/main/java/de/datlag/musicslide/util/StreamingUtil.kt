package de.datlag.musicslide.util

import android.content.Context
import android.content.IntentFilter
import de.datlag.musicslide.R
import de.datlag.musicslide.receiver.MusicReceiver
import de.datlag.musicslide.receiver.SpotifyReceiver
import de.datlag.musicslide.util.ReceiverUtil.Companion.register
import de.datlag.musicslide.util.ReceiverUtil.Companion.unregister

class StreamingUtil {

    companion object {

        private val spotifyReceiver = SpotifyReceiver()
        private val musicReceiver = MusicReceiver()

        fun registerReceiver(context: Context) {
            val spotifyActiveFilter = context.getString(R.string.spotify_package)+context.getString(R.string.active)
            val spotifyPlaybackFilter = context.getString(R.string.spotify_package)+context.getString(R.string.playback_state_change)
            val spotifyQueueFilter = context.getString(R.string.spotify_package)+context.getString(R.string.queue_change)
            val spotifyMetaFilter = context.getString(R.string.spotify_package)+context.getString(R.string.meta_changed)

            val spotifyFilter = IntentFilter(spotifyActiveFilter)
            spotifyFilter.addAction(spotifyPlaybackFilter)
            spotifyFilter.addAction(spotifyQueueFilter)
            spotifyFilter.addAction(spotifyMetaFilter)

            val musicFilter = IntentFilter()
            val defaultFilter = defaultMusicFilter(context)
            for (filter in defaultFilter.first) {
                musicFilter.addAction(filter)
            }

            context.register(spotifyReceiver, spotifyFilter, true)
            context.register(musicReceiver, musicFilter, true)
        }

        fun unregisterReceiver(context: Context) {
            context.unregister(spotifyReceiver)
            context.unregister(musicReceiver)
        }

        fun defaultMusicFilter(context: Context): Pair<Array<String>, Array<String>> {
            val packageArray: Array<String> = arrayOf(
                context.getString(R.string.android_package),
                context.getString(R.string.htc_package),
                context.getString(R.string.fm_package),
                context.getString(R.string.sec_package),
                context.getString(R.string.winamp_package),
                context.getString(R.string.amazon_package),
                context.getString(R.string.deezer_package),
                context.getString(R.string.miui_package),
                context.getString(R.string.real_package),
                context.getString(R.string.sony_package),
                context.getString(R.string.radio_package),
                context.getString(R.string.samsung_package),
                context.getString(R.string.apollo_package)
            )

            val actionArray: Array<String> = arrayOf(
                context.getString(R.string.active),
                context.getString(R.string.playback_complete),
                context.getString(R.string.playstate_changed),
                context.getString(R.string.playback_state_change),
                context.getString(R.string.queue_change),
                context.getString(R.string.meta_data_change),
                context.getString(R.string.meta_changed)
            )

            val combinedArrayList: ArrayList<String> = ArrayList()

            for (packageName in packageArray) {
                for (actionName in actionArray) {
                    combinedArrayList.add(packageName+actionName)
                }
            }

            return Pair(combinedArrayList.toTypedArray(), packageArray)
        }
    }

}