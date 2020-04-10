package de.datlag.musicslide.extend

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import io.github.inflationx.viewpump.ViewPumpContextWrapper

abstract class AdvancedActivity : AppCompatActivity() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    fun <ViewT: View> Activity.bindView(@IdRes idRes: Int): Lazy<ViewT> {
        return lazy(LazyThreadSafetyMode.NONE) {
            findViewById<ViewT>(idRes)
        }
    }

}