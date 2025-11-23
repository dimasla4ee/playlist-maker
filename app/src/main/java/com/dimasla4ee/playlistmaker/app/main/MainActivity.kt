package com.dimasla4ee.playlistmaker.app.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.presentation.util.setupWindowInsets
import com.dimasla4ee.playlistmaker.core.presentation.util.show
import com.dimasla4ee.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainer
        ) as NavHostFragment

        val navController = navHostFragment.navController

        with(binding) {
            root.setupWindowInsets { windowInsets ->
                val isKeyboardOpened = windowInsets.isVisible(WindowInsetsCompat.Type.ime())

                if (navController.currentDestination?.id == R.id.searchFragment) {
                    bottomNavigation.show(!isKeyboardOpened)
                    bottomNavDivider.show(!isKeyboardOpened)
                }
            }

            bottomNavigation.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNavigation.show(destination.id != R.id.playerFragment)
            }
        }
    }
}