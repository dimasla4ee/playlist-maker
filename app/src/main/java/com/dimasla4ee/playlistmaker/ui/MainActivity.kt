package com.dimasla4ee.playlistmaker.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dimasla4ee.playlistmaker.databinding.ActivityMainBinding
import com.dimasla4ee.playlistmaker.presentation.util.InsetsTypes
import com.dimasla4ee.playlistmaker.presentation.util.setupWindowInsets

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            enableEdgeToEdge()
            root.setupWindowInsets(InsetsTypes.StatusBars)
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            searchButton.setOnClickListener {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            mediaLibraryButton.setOnClickListener {
                val intent = Intent(this@MainActivity, MediaLibraryActivity::class.java)
                startActivity(intent)
            }

            settingsButton.setOnClickListener {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }
}