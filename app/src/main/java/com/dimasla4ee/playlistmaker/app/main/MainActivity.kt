package com.dimasla4ee.playlistmaker.app.main

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.presentation.receiver.OnConnectivityChangeReceiver
import com.dimasla4ee.playlistmaker.core.utils.setupWindowInsets
import com.dimasla4ee.playlistmaker.core.utils.show
import com.dimasla4ee.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var onConnectivityChangeReceiver: OnConnectivityChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        onConnectivityChangeReceiver = OnConnectivityChangeReceiver()
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
                val shouldShowNavigationBar = when (destination.id) {
                    R.id.playerFragment,
                    R.id.newPlaylistFragment,
                    R.id.playlistDetailedFragment -> false

                    else -> true
                }

                bottomNavigation.show(shouldShowNavigationBar)
                bottomNavDivider.show(shouldShowNavigationBar)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            this,
            onConnectivityChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(onConnectivityChangeReceiver)
    }

}
