package com.dimasla4ee.playlistmaker.feature.media_library.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dimasla4ee.playlistmaker.core.presentation.util.setupWindowInsets
import com.dimasla4ee.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaLibraryBinding.inflate(layoutInflater).apply {
            setContentView(root)
            enableEdgeToEdge()
            root.setupWindowInsets()
        }

        with(binding) {
            appBar.setNavigationOnClickListener {
                finish()
            }

            viewPager.adapter = MediaLibraryPagerAdapter(supportFragmentManager, lifecycle)

            tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Избранные треки"
                    1 -> tab.text = "Плейлисты"
                }
            }
            tabMediator.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}