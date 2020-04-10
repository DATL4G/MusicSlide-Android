package de.datlag.musicslide.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class LockTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.alpha = if (position <= -1f || position >= 1f) 0f else 1f
    }

}