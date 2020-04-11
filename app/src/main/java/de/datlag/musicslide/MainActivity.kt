package de.datlag.musicslide

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.datlag.musicslide.extend.AdvancedActivity


class MainActivity : AdvancedActivity() {

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

    companion object {
    }
}
