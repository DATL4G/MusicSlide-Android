package de.datlag.musicslide.util

import android.app.Activity
import android.content.Context
import com.deezer.sdk.model.Permissions
import com.deezer.sdk.network.connect.DeezerConnect
import com.deezer.sdk.network.connect.SessionStore
import com.deezer.sdk.network.connect.event.DialogListener
import de.datlag.musicslide.R

class DeezerUtil {

    companion object {

        private val DEFAULT_PERMISSIONS = arrayOf(Permissions.BASIC_ACCESS)

        var deezerConnect: DeezerConnect? = null
            private set

        fun connect(activity: Activity, listener: DialogListener, permissions: Array<String> = DEFAULT_PERMISSIONS) {
            deezerConnect = DeezerConnect(activity, activity.getString(R.string.deezer_id))

            deezerConnect?.authorize(activity, DEFAULT_PERMISSIONS, listener)
            val sessionStore = SessionStore()
            sessionStore.save(deezerConnect, activity)
        }

        fun restore(context: Context) {
            val sessionStore = SessionStore()
            sessionStore.restore(deezerConnect, context)
        }

    }

}