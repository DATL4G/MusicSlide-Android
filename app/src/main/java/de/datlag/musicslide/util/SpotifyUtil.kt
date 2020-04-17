package de.datlag.musicslide.util

import android.content.Context
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import de.datlag.musicslide.R
import de.datlag.musicslide.extend.AdvancedActivity
import java.util.*

class SpotifyUtil {

    companion object {

        private var appRemote: SpotifyAppRemote? = null
        var lastBeat: Calendar = Calendar.getInstance().apply { add(Calendar.MINUTE, -20) }
        var trackDuration: Long = 0
        var isPlaying: Boolean = false

        fun removeAccess(advancedActivity: AdvancedActivity) {
            advancedActivity.browserIntent(advancedActivity.getString(R.string.spotify_apps))
        }

        fun connectionBuilder(context: Context): ConnectionParams.Builder {
            return ConnectionParams.Builder(context.getString(R.string.spotify_id)).setRedirectUri(context.getString(R.string.github_repo)).setAuthMethod(ConnectionParams.AuthMethod.APP_ID)
        }

        fun connect(context: Context, connectionParams: ConnectionParams, changeListener: ChangeListener? = null) {
            SpotifyAppRemote.connect(context, connectionParams, object: Connector.ConnectionListener {

                override fun onFailure(p0: Throwable?) {
                    appRemote = null
                    changeListener?.onChanged(appRemote)
                }

                override fun onConnected(p0: SpotifyAppRemote?) {
                    appRemote = p0
                    changeListener?.onChanged(appRemote)
                }

            })
        }

        fun getAppRemote(): SpotifyAppRemote? {
            return appRemote
        }

        fun isConnected(spotifyAppRemote: SpotifyAppRemote? = appRemote): Boolean {
            return spotifyAppRemote != null && spotifyAppRemote.isConnected
        }

    }

    interface ChangeListener {
        fun onChanged(spotifyAppRemote: SpotifyAppRemote?)
    }

}