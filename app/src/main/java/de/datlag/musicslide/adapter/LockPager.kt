package de.datlag.musicslide.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.datlag.musicslide.fragments.LockFragment
import de.datlag.musicslide.fragments.MusicFragment

class LockPager(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return LOCK_ITEM_SIZE
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            1 -> MusicFragment.newInstance()
            else -> LockFragment.newInstance()
        }
    }

    companion object {
        const val LOCK_ITEM_SIZE = 2
    }

}