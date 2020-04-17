package de.datlag.musicslide.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.datlag.musicslide.util.MusicUtil
import de.datlag.musicslide.util.StreamingUtil
import java.util.*


class MusicReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.let {
            if (context !== null) {
                val allowed = isAllowedAction(context, it.action)
                if (allowed.first) {
                    updateMusicData(it, allowed.second)
                }
            }
        }

    }

    private fun updateMusicData(intent: Intent, packageName: String) {
        val artist = intent.getStringExtra("artist")
        val album = intent.getStringExtra("album")
        val track = intent.getStringExtra("track")
        val playing = intent.getBooleanExtra("playing", false)

        MusicUtil.artist = artist ?: "No Artist"
        MusicUtil.album = album ?: "No Album"
        MusicUtil.track = track ?: "No Track"
        MusicUtil.playing = playing
        MusicUtil.lastBeat = Calendar.getInstance()
        MusicUtil.packageName = packageName
    }

    private fun isAllowedAction(context: Context, action: String?): Pair<Boolean, String> {
        val defaultFilter = StreamingUtil.defaultMusicFilter(context)
        var returnBool = false
        var returnString = defaultFilter.second[0]

            for ((index, actionName) in defaultFilter.first.withIndex()) {
                if (action == actionName) {
                    returnBool = true
                    returnString = defaultFilter.second[index]
                }
            }

        return Pair(returnBool, returnString)
    }

}