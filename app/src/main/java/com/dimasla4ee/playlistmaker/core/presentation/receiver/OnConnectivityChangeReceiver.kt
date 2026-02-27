package com.dimasla4ee.playlistmaker.core.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.data.network.NetworkStatusProvider

class OnConnectivityChangeReceiver : BroadcastReceiver() {

    private val action = ConnectivityManager.CONNECTIVITY_ACTION

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != action) return

        val networkStatusProvider = NetworkStatusProvider(context ?: return)
        if (!networkStatusProvider.isConnected) {
            val message = context.getString(R.string.no_internet_access)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun register(context: Context) = ContextCompat.registerReceiver(
        context,
        this,
        IntentFilter(action),
        ContextCompat.RECEIVER_NOT_EXPORTED
    )

    fun unregister(context: Context) = context.unregisterReceiver(this)

}
