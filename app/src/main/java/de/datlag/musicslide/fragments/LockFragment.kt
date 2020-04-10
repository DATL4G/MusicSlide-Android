package de.datlag.musicslide.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.datlag.musicslide.R

class LockFragment : Fragment() {

    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        count = arguments?.getInt(ARG_COUNT) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.section_label).text = "Fragment: $count"
    }

    companion object {
        private const val ARG_COUNT = "param1"

        fun newInstance(count: Int): LockFragment {
            val lockFragment = LockFragment()
            val args = Bundle()
            args.putInt(ARG_COUNT, count)
            lockFragment.arguments = args
            return lockFragment
        }
    }

}