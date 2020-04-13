package de.datlag.musicslide.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import de.datlag.musicslide.R

class CommonUtil {

    companion object {

        fun enterFullScreen(window: Window) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        fun useNotchSpace(window: Window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }

        fun setStatusBarColor(window: Window, color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = color
            }
        }

        @SuppressLint("SourceLockedOrientationActivity")
        fun requestPortrait(activity: Activity) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        fun hideSystemUI(window: Window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE)
            } else {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
            }
        }

        fun showSystemUI(window: Window) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }

        fun View.applyScaleClick(scale: Float = 0.9F) {
            val scaleDownX = ObjectAnimator.ofFloat(this, this.context.getString(R.string.animator_scaleX), scale)
            val scaleDownY = ObjectAnimator.ofFloat(this, this.context.getString(R.string.animator_scaleY), scale)
            val scaleDown = AnimatorSet()
            scaleDown.play(scaleDownX).with(scaleDownY)

            val scaleUpX = ObjectAnimator.ofFloat(this, this.context.getString(R.string.animator_scaleX), this.scaleX)
            val scaleUpY = ObjectAnimator.ofFloat(this, this.context.getString(R.string.animator_scaleY), this.scaleY)
            val scaleUp = AnimatorSet()
            scaleUp.play(scaleUpX).with(scaleUpY)

            var shouldClick = false

            this.setOnTouchListener(View.OnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        scaleDown.start()
                        shouldClick = true
                    }
                    MotionEvent.ACTION_UP -> {
                        scaleUp.start()
                        if (shouldClick)
                            performClick()
                        shouldClick = false
                    }
                    else -> {}
                }
                return@OnTouchListener true
            })
        }
    }

}