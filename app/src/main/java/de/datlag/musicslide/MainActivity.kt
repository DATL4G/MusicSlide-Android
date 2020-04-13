package de.datlag.musicslide

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
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
import de.datlag.musicslide.util.SaveUtil.Companion.saveBool
import de.datlag.musicslide.util.SaveUtil.Companion.getBool
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AdvancedActivity() {

    private val activity: AdvancedActivity = this@MainActivity
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        writeSharedPrefs()

        CommonUtil.enterFullScreen(window)
        CommonUtil.showSystemUI(window)
        CommonUtil.useNotchSpace(window)
        CommonUtil.setStatusBarColor(window, ContextCompat.getColor(this, R.color.statusBarColor))
        CommonUtil.requestPortrait(this)
        askPermission()

        applySwitches()

        linkSpotify.applyScaleClick()
        linkDeezer.applyScaleClick()
    }

    private fun writeSharedPrefs() {
        saveBool(getString(R.string.appearance), getBool(getString(R.string.appearance), true))
        saveBool(getString(R.string.buttons_usable), getBool(getString(R.string.buttons_usable), true))
        saveBool(getString(R.string.skip_usable), getBool(getString(R.string.skip_usable), true))
    }

    private fun askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.draw_title))
                    .setMessage(getString(R.string.draw_message))
                    .setPositiveButton(getString(R.string.grant)) { _, _ ->
                        startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse(getString(R.string.packageParam)+packageName)))
                    }
                    .setNegativeButton(getString(R.string.close)) { _, _ ->
                        finishAffinity()
                    }
                    .create().show()
            }
        }
    }

    private fun matchSpotifyLinkButton(spotifyAppRemote: SpotifyAppRemote?) {
        linkSpotify.isEnabled = SpotifyAppRemote.isSpotifyInstalled(this)
        linkSpotify.text = if (SpotifyUtil.isConnected(spotifyAppRemote)) getString(R.string.disconnect) else getString(R.string.connect)
        linkSpotify.setOnClickListener {
            if (spotifyAppRemote != null && spotifyAppRemote.isConnected) {
                SpotifyUtil.removeAccess(activity)
            } else {
                val connectionParams = SpotifyUtil.connectionBuilder(activity)
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

    private fun applySwitches() {
        switchAppearance.setOnCheckedChangeListener { isChecked ->
            saveBool(getString(R.string.appearance), isChecked)
        }

        switchSkip.setOnCheckedChangeListener { isChecked ->
            saveBool(getString(R.string.skip_usable), isChecked)
        }

        switchButton.setOnCheckedChangeListener { isChecked ->
            saveBool(getString(R.string.buttons_usable), isChecked)
            if (switchSkip.isChecked)
                switchSkip.setChecked(isChecked, false)
            skipEnableState(isChecked)
        }

        switchAppearance.setChecked(getBool(getString(R.string.appearance), true), false)
        switchSkip.setChecked(getBool(getString(R.string.skip_usable), true), false)
        switchButton.setChecked(getBool(getString(R.string.buttons_usable), true), false)
        skipEnableState(switchButton.isChecked)
    }

    private fun skipEnableState(isEnabled: Boolean) {
        switchSkip.isEnabled = isEnabled
        if (!isEnabled) {
            switchSkip.onColor = Color.GRAY
            switchSkip.offColor = Color.GRAY
        } else {
            switchSkip.onColor = ContextCompat.getColor(activity, R.color.buttonColor)
            switchSkip.offColor = ContextCompat.getColor(activity, R.color.buttonColor )
        }
        switchSkip.applyColorChange()
    }

    override fun onStart() {
        super.onStart()
        BootUtil.registerReceiver(this)
        StreamingUtil.registerReceiver(this)

        val connectionParams = SpotifyUtil.connectionBuilder(activity)
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
