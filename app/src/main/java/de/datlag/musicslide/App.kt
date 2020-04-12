package de.datlag.musicslide

import android.app.Application
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.spotify.android.appremote.api.SpotifyAppRemote
import de.datlag.musicslide.receiver.BootReceiver
import de.datlag.musicslide.receiver.SpotifyReceiver
import de.datlag.musicslide.services.LockService
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class App : Application() {

    //persistence
    val spotify = Spotify()

    class Spotify {
        var spotifyAppRemote: SpotifyAppRemote? = null
    }

    override fun onCreate() {
        super.onCreate()

        startServices()
        streamingReceiver()
        applyFont()
    }

    private fun applyFont() {
        ViewPump.init(ViewPump.builder()
            .addInterceptor(CalligraphyInterceptor(
                CalligraphyConfig.Builder()
                    .setDefaultFontPath(this.getString(R.string.font_path))
                    .setFontAttrId(R.attr.fontPath)
                    .build()))
            .build())
    }

    private fun startServices() {
        registerReceiver(BootReceiver(), IntentFilter(Intent.ACTION_BOOT_COMPLETED))
        notKillReceiver(BootReceiver::class.java)

        ContextCompat.startForegroundService(this, Intent(this, LockService::class.java))
        startService(Intent(this, LockService::class.java))
    }

    private fun streamingReceiver() {
        val spotifyFilter = IntentFilter("com.spotify.music.active")
        spotifyFilter.addAction("com.spotify.music.playbackstatechanged")
        spotifyFilter.addAction("com.spotify.music.metadatachanged")
        spotifyFilter.addAction("com.spotify.music.queuechanged")

        registerReceiver(SpotifyReceiver(), spotifyFilter)
        notKillReceiver(SpotifyReceiver::class.java)
    }

    private fun notKillReceiver(clazz: Class<out BroadcastReceiver>) {
        val newReceiver = ComponentName(applicationContext.packageName, clazz.name)
        if (packageManager.getComponentEnabledSetting(newReceiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
            packageManager.setComponentEnabledSetting(newReceiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
    }

}