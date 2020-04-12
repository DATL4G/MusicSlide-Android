package de.datlag.musicslide.fragments

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import de.datlag.musicslide.R
import de.datlag.musicslide.util.SpotifyUtil
import kotlinx.android.synthetic.main.fragment_music.*
import java.util.*

class MusicFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (moveRight.drawable as Animatable).start()
    }

    override fun onResume() {
        super.onResume()
        connectStreamServices()
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(SpotifyUtil.getAppRemote())
    }

    private fun connectStreamServices() {
        val appRemote: SpotifyAppRemote? = SpotifyUtil.getAppRemote()
        if (appRemote == null || !appRemote.isConnected) {
            val connectionParams = SpotifyUtil.connectionBuilder()
                .showAuthView(false)
                .build()
            SpotifyUtil.connect(requireContext(), connectionParams, object: SpotifyUtil.ChangeListener{
                override fun onChanged(spotifyAppRemote: SpotifyAppRemote?) {
                    setMusicData()
                }
            })
        } else {
            setMusicData()
        }
    }

    private fun setMusicData() {
        val spotifyAppRemote = SpotifyUtil.getAppRemote()

        spotifyAppRemote?.playerApi?.playerState?.setResultCallback {
            setSpotifyData(spotifyAppRemote, it)
            SpotifyUtil.lastBeat = Calendar.getInstance()
        }
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            setSpotifyData(spotifyAppRemote, it)
            SpotifyUtil.lastBeat = Calendar.getInstance()
        }
    }

    private fun setSpotifyData(spotifyAppRemote: SpotifyAppRemote, playerState: PlayerState) {
        trackText.text = playerState.track.name
        artistText.text = playerState.track.artist.name
        spotifyAppRemote.imagesApi.getImage(playerState.track.imageUri).setResultCallback {
            trackImage.setImageBitmap(it)
        }
        skipPrevious.setOnClickListener {
            spotifyAppRemote.playerApi.skipPrevious()
        }
        skipNext.setOnClickListener {
            spotifyAppRemote.playerApi.skipNext()
        }
        playPause.apply {
            if (playerState.isPaused) {
                setDrawable(R.drawable.ic_play_circle_filled_white_24dp)
                setOnClickListener {
                    spotifyAppRemote.playerApi.resume()
                    setDrawable(R.drawable.ic_pause_circle_filled_white_24dp)
                }
            } else {
                setDrawable(R.drawable.ic_pause_circle_filled_white_24dp)
                setOnClickListener {
                    spotifyAppRemote.playerApi.pause()
                    setDrawable(R.drawable.ic_play_circle_filled_white_24dp)
                }
            }
        }
    }

    private fun ImageView.setDrawable(@DrawableRes resource: Int) {
        setImageDrawable(ContextCompat.getDrawable(requireContext(), resource))
    }

    companion object {
        fun newInstance(): MusicFragment {
            return MusicFragment()
        }
    }

}