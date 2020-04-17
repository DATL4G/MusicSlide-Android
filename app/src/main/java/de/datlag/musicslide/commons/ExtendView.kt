package de.datlag.musicslide.commons

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import de.datlag.musicslide.R

fun View.scaleClick(scale: Float = 0.9F) {
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