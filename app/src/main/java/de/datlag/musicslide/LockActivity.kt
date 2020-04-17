package de.datlag.musicslide

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import de.datlag.musicslide.adapter.LockPager
import de.datlag.musicslide.commons.enterFullScreen
import de.datlag.musicslide.commons.getBool
import de.datlag.musicslide.commons.hideSystemUI
import de.datlag.musicslide.commons.useNotchSpace
import de.datlag.musicslide.extend.AdvancedActivity
import de.datlag.musicslide.transformer.LockTransformer
import de.datlag.musicslide.util.BootUtil
import de.datlag.musicslide.util.StreamingUtil


class LockActivity : AdvancedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)

        setLockAppearance()
        window.enterFullScreen()
        window.useNotchSpace()
        window.hideSystemUI()
        requestPortrait()
        pagerSetup()

        if (!getBool(getString(R.string.appearance), false)) {
            finishAffinity()
        }
    }

    private fun pagerSetup() {
        var scrollState = 0
        var pos = 1
        var offset = 0.5F

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = LockPager(this)
        viewPager.currentItem = pos
        viewPager.setPageTransformer(LockTransformer())
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                pos = position
                exit(pos, scrollState, offset)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                scrollState = state
                exit(pos, scrollState, offset)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                offset = positionOffset
                exit(pos, scrollState, offset)
            }

        })
    }

    private fun exit(pos: Int, scrollState: Int, offset: Float) {
        if ((pos == 0 && scrollState == 2) && (offset > 0.9F || offset == 0.0F)) {
            finishAffinity()
        }
    }

    @Suppress("DEPRECATION")
    private fun setLockAppearance() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val keyManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        }
    }

    override fun onStart() {
        super.onStart()
        BootUtil.registerReceiver(this)
        StreamingUtil.registerReceiver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        BootUtil.unregisterReceiver(this)
        StreamingUtil.unregisterReceiver(this)
    }
}