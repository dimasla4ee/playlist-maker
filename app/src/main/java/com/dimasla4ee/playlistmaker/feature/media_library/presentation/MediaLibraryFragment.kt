package com.dimasla4ee.playlistmaker.feature.media_library.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.utils.viewBinding
import com.dimasla4ee.playlistmaker.databinding.FragmentMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryFragment : Fragment(R.layout.fragment_media_library) {

    private val binding by viewBinding(FragmentMediaLibraryBinding::bind)
    private var tabMediator: TabLayoutMediator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            viewPager.adapter = MediaLibraryPagerAdapter(childFragmentManager, lifecycle)

            tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.favorite_tracks)
                    1 -> tab.text = getString(R.string.playlists)
                }
            }.apply { attach() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
        tabMediator = null
    }

}