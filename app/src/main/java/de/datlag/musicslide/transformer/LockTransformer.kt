package de.datlag.musicslide.transformer

import android.view.View
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2

class LockTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            when {
                position < -1 -> {
                    alpha = 0f
                }
                position <= 0 -> {
                    alpha = 1 - position
                    translationX = width * -position
                    ViewCompat.setTranslationZ(this, -1f)
                }
                position <= 1 -> {
                    alpha = 1f
                    translationX = 0f
                    ViewCompat.setTranslationZ(this, 0f)
                }
                else -> {
                    alpha = 0f
                }
            }
        }
    }

}