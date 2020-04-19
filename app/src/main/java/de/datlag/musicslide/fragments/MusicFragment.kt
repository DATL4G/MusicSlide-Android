package de.datlag.musicslide.fragments

import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.makeramen.roundedimageview.RoundedImageView
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import de.datlag.musicslide.R
import de.datlag.musicslide.commons.getBool
import de.datlag.musicslide.commons.scaleClick
import de.datlag.musicslide.util.MusicUtil
import de.datlag.musicslide.util.SpotifyUtil
import kotlinx.android.synthetic.main.fragment_music.*
import java.util.*

class MusicFragment : Fragment() {

    private var trackTextView: AppCompatTextView? = null
    private var artistTextView: AppCompatTextView? = null
    private var trackImageView: RoundedImageView? = null
    private var skipPreviousButton: AppCompatImageView? = null
    private var skipNextButton: AppCompatImageView? = null
    private var playPauseButton: AppCompatImageView? = null
    private var trackControlLayout: ConstraintLayout? = null
    private var adView: AdView? = null

    private lateinit var con: Context
    private var musicChangeListener: MusicUtil.ChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        con = context ?: activity ?: requireContext()
        if (!con.getBool(getString(R.string.appearance), false)) {
            requireActivity().finishAffinity()
        }

        MobileAds.initialize(con)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (!con.getBool(getString(R.string.alternative_layout), false)) {
            inflater.inflate(R.layout.fragment_music, container, false)
        } else {
            inflater.inflate(R.layout.fragment_alternative_music, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (moveRight.drawable as Animatable).start()
        trackTextView = view.findViewById(R.id.trackText)
        artistTextView = view.findViewById(R.id.artistText)
        trackImageView = view.findViewById(R.id.trackImage)
        skipPreviousButton = view.findViewById(R.id.skipPrevious)
        skipNextButton = view.findViewById(R.id.skipNext)
        playPauseButton = view.findViewById(R.id.playPause)
        trackControlLayout = view.findViewById(R.id.trackControl)
        adView = view.findViewById(R.id.adView)
    }

    override fun onResume() {
        super.onResume()
        if (SpotifyAppRemote.isSpotifyInstalled(con)) {
            connectSpotify()
        }
        androidConnect()
        adView?.loadAd(AdRequest.Builder().build())
    }

    override fun onStart() {
        super.onStart()
        if (SpotifyAppRemote.isSpotifyInstalled(con)) {
            connectSpotify()
        }
        androidConnect()
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(SpotifyUtil.getAppRemote())
    }

    private fun connectSpotify() {
        val appRemote: SpotifyAppRemote? = SpotifyUtil.getAppRemote()
        if (appRemote == null || !appRemote.isConnected) {
            val connectionParams = SpotifyUtil.connectionBuilder(con)
                .showAuthView(false)
                .build()
            SpotifyUtil.connect(con, connectionParams, object : SpotifyUtil.ChangeListener {
                override fun onChanged(spotifyAppRemote: SpotifyAppRemote?) {
                    spotifyData(spotifyAppRemote)
                }
            })
        } else {
            spotifyData(appRemote)
        }
    }

    private fun spotifyData(spotifyAppRemote: SpotifyAppRemote?) {
        spotifyAppRemote?.playerApi?.playerState?.setResultCallback {
            SpotifyUtil.lastBeat = Calendar.getInstance()
            SpotifyUtil.trackDuration = it.track.duration
            SpotifyUtil.isPlaying = !it.isPaused

            if (!MusicUtil.playing) {
                spotifyViewData(spotifyAppRemote, it)
            }

        }
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            SpotifyUtil.lastBeat = Calendar.getInstance()
            SpotifyUtil.trackDuration = it.track.duration
            SpotifyUtil.isPlaying = !it.isPaused

            if (!MusicUtil.playing) {
                spotifyViewData(spotifyAppRemote, it)
            }

        }
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setErrorCallback {
            if (!MusicUtil.playing) {
                requireActivity().finishAffinity()
            }
        }
        spotifyAppRemote?.playerApi?.subscribeToPlayerContext()?.setErrorCallback {
            if (!MusicUtil.playing) {
                requireActivity().finishAffinity()
            }
        }
    }

    private fun spotifyViewData(spotifyAppRemote: SpotifyAppRemote, playerState: PlayerState) {
        viewData(playerState.track.name, playerState.track.artist.name)

        spotifyAppRemote.imagesApi.getImage(playerState.track.imageUri).setResultCallback {
            trackImageView?.setImageBitmap(it)
        }

        skipPreviousButton?.setOnClickListener {
            if (buttonsUsable(true)) {
                spotifyAppRemote.playerApi.skipPrevious()
            }
        }

        skipNextButton?.setOnClickListener {
            if (buttonsUsable(true)) {
                spotifyAppRemote.playerApi.skipNext()
            }
        }

        playPauseButton?.apply {
            if (playerState.isPaused) {
                setDrawable(R.drawable.ic_play_circle_filled_white_24dp)
                setOnClickListener {
                    if (buttonsUsable()) {
                        spotifyAppRemote.playerApi.resume()
                        setDrawable(R.drawable.ic_pause_circle_filled_white_24dp)
                    }
                }
            } else {
                setDrawable(R.drawable.ic_pause_circle_filled_white_24dp)
                setOnClickListener {
                    if (buttonsUsable()) {
                        spotifyAppRemote.playerApi.pause()
                        setDrawable(R.drawable.ic_play_circle_filled_white_24dp)
                    }
                }
            }
        }
    }

    private fun androidConnect() {
        if (musicChangeListener == null) {
            musicChangeListener = object : MusicUtil.ChangeListener {
                override fun onChange() {
                    if (!SpotifyUtil.isPlaying) {
                        androidData()
                    }
                }
            }
            MusicUtil.addListener(con, musicChangeListener!!)
        }
    }

    private fun androidData() {
        viewData(MusicUtil.track, MusicUtil.artist)

        trackImageView?.setDrawable(R.drawable.ic_audiotrack_white_24dp)

        skipPreviousButton?.setOnClickListener {
            if (buttonsUsable(true)) {
                MusicUtil.control(con, 3)
            }
        }

        skipNextButton?.setOnClickListener {
            if (buttonsUsable(true)) {
                MusicUtil.control(con, 2)
            }
        }

        playPauseButton?.apply {
            if (MusicUtil.playing) {
                setDrawable(R.drawable.ic_pause_circle_filled_white_24dp)
                setOnClickListener {
                    if (buttonsUsable()) {
                        MusicUtil.control(con, 1)
                        setDrawable(R.drawable.ic_play_circle_filled_white_24dp)
                    }
                }
            } else {
                setDrawable(R.drawable.ic_play_circle_filled_white_24dp)
                setOnClickListener {
                    if (buttonsUsable()) {
                        MusicUtil.control(con, 0)
                        setDrawable(R.drawable.ic_pause_circle_filled_white_24dp)
                    }
                }
            }
        }
    }

    private fun viewData(track: String, artist: String) {
        trackTextView?.text = track
        artistTextView?.text = artist
        skipPreviousButton?.scaleClick(0.8F)
        skipNextButton?.scaleClick(0.8F)
        playPauseButton?.scaleClick(0.8F)

        trackControlLayout?.visibility = View.VISIBLE
    }


    private fun buttonsUsable(checkSkip: Boolean = false): Boolean {
        return if (!checkSkip) {
            con.getBool(getString(R.string.buttons_usable), false)
        } else {
            buttonsUsable(false) && con.getBool(getString(R.string.skip_usable), false)
        }
    }

    private fun ImageView.setDrawable(@DrawableRes resource: Int) {
        setImageDrawable(ContextCompat.getDrawable(con, resource))
    }

    companion object {
        fun newInstance(): MusicFragment {
            return MusicFragment()
        }
    }

}