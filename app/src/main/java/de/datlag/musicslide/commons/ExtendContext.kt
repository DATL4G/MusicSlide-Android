package de.datlag.musicslide.commons

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import com.spotify.android.appremote.api.SpotifyAppRemote
import de.datlag.musicslide.R
import de.datlag.musicslide.util.MusicUtil

fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        this.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (ignored: Exception) {
        false
    }
}

fun Context.saveBool(key: String, value: Boolean) {
    getSharedPrefs(this).edit().putBoolean(key, value).apply()
}

fun Context.getBool(key: String, default: Boolean): Boolean {
    return getSharedPrefs(this).getBoolean(key, default)
}

fun Context.checkQuitLockScreen(): Boolean {
    return (!isMusicPlaying(true) && !SpotifyAppRemote.isSpotifyInstalled(this)) || !this.getBool(
        getString(R.string.appearance),
        false
    )
}

fun Context.isMusicPlaying(receiverNotified: Boolean = false): Boolean {
    return if (receiverNotified) {
        MusicUtil.playing
    } else {
        val audioManager: AudioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.isMusicActive
    }
}

private fun getSharedPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
}