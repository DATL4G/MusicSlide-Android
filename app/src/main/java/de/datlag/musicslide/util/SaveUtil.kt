package de.datlag.musicslide.util

import android.content.Context
import android.content.SharedPreferences

class SaveUtil {

    companion object {

        fun Context.saveBool(key: String, value: Boolean) {
            getSharedPrefs(this).edit().putBoolean(key, value).apply()
        }

        fun Context.getBool(key: String, default: Boolean): Boolean {
            return getSharedPrefs(this).getBoolean(key, default)
        }

        private fun getSharedPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        }

    }

}