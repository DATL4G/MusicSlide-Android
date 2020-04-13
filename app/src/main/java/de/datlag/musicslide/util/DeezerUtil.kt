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

        private fun createConnection(context: Context) {
            deezerConnect = DeezerConnect(context.applicationContext, context.getString(R.string.deezer_id))
        }

        fun connect(activity: Activity, listener: DialogListener, permissions: Array<String> = DEFAULT_PERMISSIONS) {
            createConnection(activity)
            deezerConnect?.authorize(activity, permissions, listener)
            val sessionStore = SessionStore()
            sessionStore.save(deezerConnect, activity.applicationContext)
        }

        fun restore(context: Context) {
            if (deezerConnect == null) {
                createConnection(context)
            }
            val sessionStore = SessionStore()
            sessionStore.restore(deezerConnect, context.applicationContext)
        }

        fun isConnected(deezerConnection: DeezerConnect? = deezerConnect): Boolean {
            return deezerConnection != null && deezerConnection.isSessionValid
        }

        fun removeAccess(context: Context) {
            deezerConnect?.logout(context)
        }

    }

}