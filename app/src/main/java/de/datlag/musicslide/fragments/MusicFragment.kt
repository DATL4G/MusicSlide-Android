package de.datlag.musicslide.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.datlag.musicslide.R
import rm.com.audiowave.AudioWaveView

class MusicFragment : Fragment() {

    private lateinit var audioWaveView: AudioWaveView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        audioWaveView = view.findViewById(R.id.audioWave)
    }

    private fun setMusicData() {
        audioWaveView.onProgressChanged = { progress, byUser ->

        }
    }

    companion object {
        fun newInstance(): MusicFragment {
            return MusicFragment()
        }
    }

}