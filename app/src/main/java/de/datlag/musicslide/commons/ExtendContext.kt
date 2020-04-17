package de.datlag.musicslide.commons

import android.content.Context
import android.content.SharedPreferences

fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        this.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (ignored: Exception) {
        false
    }
}

fun Context.saveBool(key: String, value: Boolean) {
    getSharedPrefs(this).edit().putBoolean(key, value).apply()
}

fun Context.getBool(key: String, default: Boolean): Boolean {
    return getSharedPrefs(this).getBoolean(key, default)
}

private fun getSharedPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
}