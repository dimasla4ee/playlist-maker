package com.dimasla4ee.playlistmaker.feature.media_library.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaLibraryPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> FavoriteFragment()
        1 -> PlaylistFragment()
        else -> throw IllegalArgumentException("Invalid position: $position")
    }

    override fun getItemCount(): Int = 2
}