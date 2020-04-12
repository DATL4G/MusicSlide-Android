package de.datlag.musicslide.util

import android.content.Context
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import de.datlag.musicslide.extend.AdvancedActivity

class SpotifyUtil {

    companion object {

        const val CLIENT_ID = "client_id"
        const val REDIRECT_URI = "https://github.com/DATL4G/MusicSlide-Android"

        private var appRemote: SpotifyAppRemote? = null

        fun removeAccess(advancedActivity: AdvancedActivity) {
            advancedActivity.browserIntent("https://spotify.com/account/apps")
        }

        fun connectionBuilder(clientId: String = CLIENT_ID, redirectUri: String = REDIRECT_URI): ConnectionParams.Builder {
            return ConnectionParams.Builder(clientId).setRedirectUri(redirectUri).setAuthMethod(ConnectionParams.AuthMethod.APP_ID)
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

    }

    interface ChangeListener {
        fun onChanged(spotifyAppRemote: SpotifyAppRemote?)
    }

}