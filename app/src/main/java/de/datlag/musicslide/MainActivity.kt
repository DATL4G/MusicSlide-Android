package de.datlag.musicslide

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.spotify.android.appremote.api.SpotifyAppRemote
import de.datlag.musicslide.extend.AdvancedActivity
import de.datlag.musicslide.util.CommonUtil
import de.datlag.musicslide.util.SpotifyUtil


class MainActivity : AdvancedActivity() {

    private val linkSpotifyButton: AppCompatButton by bindView(R.id.linkSpotify)
    private val activity: AdvancedActivity = this@MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CommonUtil.enterFullScreen(window)
        CommonUtil.showSystemUI(window)
        CommonUtil.useNotchSpace(window)
        CommonUtil.setStatusBarColor(window, ContextCompat.getColor(this, R.color.statusBarColor))
        CommonUtil.requestPortrait(this)
        askPermission()
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
        linkSpotifyButton.isEnabled = SpotifyAppRemote.isSpotifyInstalled(this)
        linkSpotifyButton.text = if (spotifyAppRemote != null) "Disconnect" else "Connect"
        linkSpotifyButton.setOnClickListener {
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
}
