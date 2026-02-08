package com.dimasla4ee.playlistmaker.feature.media_library.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dimasla4ee.playlistmaker.feature.favorite.presentation.FavoriteFragment
import com.dimasla4ee.playlistmaker.feature.playlists.presentation.PlaylistListFragment

class MediaLibraryPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> FavoriteFragment.newInstance()
        else -> PlaylistListFragment.newInstance()
    }

    override fun getItemCount(): Int = 2
}