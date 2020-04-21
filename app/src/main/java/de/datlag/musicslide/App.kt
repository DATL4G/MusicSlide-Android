package de.datlag.musicslide

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.multidex.MultiDexApplication
import de.datlag.musicslide.services.LockService
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startServices()
        applyFont()
    }

    private fun applyFont() {
        ViewPump.init(ViewPump.builder()
            .addInterceptor(CalligraphyInterceptor(
                CalligraphyConfig.Builder()
                    .setDefaultFontPath(this.getString(R.string.font_path))
                    .setFontAttrId(R.attr.fontPath)
                    .build()))
            .build())
    }

    private fun startServices() {
        ContextCompat.startForegroundService(this, Intent(this, LockService::class.java))
        startService(Intent(this, LockService::class.java))
    }

}