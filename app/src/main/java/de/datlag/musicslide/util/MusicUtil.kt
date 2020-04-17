package de.datlag.musicslide.util

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.view.KeyEvent
import de.datlag.musicslide.R
import java.util.*

class MusicUtil {

    companion object {

        private var listener: MutableSet<ChangeListener> = mutableSetOf()
        private var context: MutableList<Context> = mutableListOf()
        var packageName: String = String()
        var lastBeat: Calendar = Calendar.getInstance().apply { add(Calendar.MINUTE, -20) }

        var artist: String = "No Artist"
            set(value) {
                if (field != value) {
                    field = value
                    listener.forEach {
                        it.onChange()
                    }
                }
            }

        var album: String = "No Album"
            set(value) {
                if (field != value) {
                    field = value
                    listener.forEach {
                        it.onChange()
                    }
                }
            }

        var track: String = "No Track"
            set(value) {
                if (field != value) {
                    field = value
                    listener.forEach {
                        it.onChange()
                    }
                }
            }

        var playing: Boolean = false
            set(value) {
                if (field != value) {
                    field = value
                    listener.forEach {
                        it.onChange()
                    }
                }
            }

        fun addListener(context: Context, listener: ChangeListener) {
            this.listener.add(listener)
            this.context.add(context)
        }

        fun control(context: Context, mode: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val audioManager: AudioManager =
                    context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                var event: KeyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)

                when (mode) {
                    0 -> event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
                    1 -> event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE)
                    2 -> event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT)
                    3 -> event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
                }

                audioManager.dispatchMediaKeyEvent(event)
            } else {
                val intent = Intent(packageName+context.getString(R.string.service_command))
                var action = context.getString(R.string.music_play)

                when(mode) {
                    0 -> action = context.getString(R.string.music_play)
                    1 -> action = context.getString(R.string.music_pause)
                    2 -> action = context.getString(R.string.music_next)
                    3 -> action = context.getString(R.string.music_previous)
                }

                intent.putExtra(packageName+context.getString(R.string.music_command), action)
                context.sendBroadcast(intent)
            }
        }
    }

    interface ChangeListener {
        fun onChange()
    }

}