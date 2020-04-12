package de.datlag.musicslide

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spotify.android.appremote.api.SpotifyAppRemote
import de.datlag.musicslide.extend.AdvancedActivity
import de.datlag.musicslide.util.BootUtil
import de.datlag.musicslide.util.CommonUtil
import de.datlag.musicslide.util.CommonUtil.Companion.applyScaleClick
import de.datlag.musicslide.util.SpotifyUtil
import de.datlag.musicslide.util.StreamingUtil
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AdvancedActivity() {

    private val activity: AdvancedActivity = this@MainActivity
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)

        CommonUtil.enterFullScreen(window)
        CommonUtil.showSystemUI(window)
        CommonUtil.useNotchSpace(window)
        CommonUtil.setStatusBarColor(window, ContextCompat.getColor(this, R.color.statusBarColor))
        CommonUtil.requestPortrait(this)
        askPermission()

        linkSpotify.applyScaleClick()
        linkDeezer.applyScaleClick()
        linkDeezer.text = "Connect"

        switchAppearance.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("appearance", isChecked).apply()
        }

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("buttons_usable", isChecked).apply()
        }

        switchAppearance.isChecked = sharedPreferences.getBoolean("appearance", true)
        switchButton.isChecked = sharedPreferences.getBoolean("buttons_usable", true)
    }

    private fun askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Draw over other Apps")
                    .setMessage("This is needed to show the Music-Configuration on LockScreen")
                    .setPositiveButton("Grant") { _, _ ->
                        startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:$packageName")))
                    }
                    .setNegativeButton("Close") { _, _ ->
                        finishAffinity()
                    }
                    .create().show()
            }
        }
    }

    private fun matchSpotifyLinkButton(spotifyAppRemote: SpotifyAppRemote?) {
        linkSpotify.isEnabled = SpotifyAppRemote.isSpotifyInstalled(this)
        linkSpotify.text = if (spotifyAppRemote != null) "Disconnect" else "Connect"
        linkSpotify.setOnClickListener {
            if (spotifyAppRemote != null && spotifyAppRemote.isConnected) {
                SpotifyUtil.removeAccess(activity)
            } else {
                val connectionParams = SpotifyUtil.connectionBuilder()
                    .showAuthView(true)
                    .build()
                SpotifyUtil.connect(activity, connectionParams, object: SpotifyUtil.ChangeListener{
                    override fun onChanged(spotifyAppRemote: SpotifyAppRemote?) {
                        matchSpotifyLinkButton(spotifyAppRemote)
                    }
                })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        BootUtil.registerReceiver(this)
        StreamingUtil.registerReceiver(this)

        val connectionParams = SpotifyUtil.connectionBuilder()
            .showAuthView(false)
            .build()
        SpotifyUtil.connect(this, connectionParams, object: SpotifyUtil.ChangeListener{
            override fun onChanged(spotifyAppRemote: SpotifyAppRemote?) {
                matchSpotifyLinkButton(spotifyAppRemote)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(SpotifyUtil.getAppRemote())
    }

    override fun onDestroy() {
        super.onDestroy()
        BootUtil.unregisterReceiver(this)
        StreamingUtil.unregisterReceiver(this)
    }
}
