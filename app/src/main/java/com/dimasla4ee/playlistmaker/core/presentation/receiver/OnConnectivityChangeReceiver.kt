package com.dimasla4ee.playlistmaker.core.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.data.network.NetworkStatusProvider

class OnConnectivityChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ConnectivityManager.CONNECTIVITY_ACTION) return

        val networkStatusProvider = NetworkStatusProvider(context ?: return)
        if (!networkStatusProvider.isConnected) {
            val message = context.getString(R.string.no_internet_access)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}
