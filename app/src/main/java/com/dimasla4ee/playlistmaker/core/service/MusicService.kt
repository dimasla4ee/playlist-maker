package com.dimasla4ee.playlistmaker.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.domain.model.PlayerController
import com.dimasla4ee.playlistmaker.core.domain.model.PlayerState
import com.dimasla4ee.playlistmaker.core.utils.KeyConstants
import com.dimasla4ee.playlistmaker.core.utils.toMmSs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MusicService : Service(), PlayerController {

    private companion object {
        val TAG: String = MusicService::class.java.simpleName
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val SERVICE_NOTIFICATION_ID = 100
        const val TIMER_DELAY = 300L
    }

    private val binder = MusicServiceBinder()
    private var mediaPlayer: MediaPlayer? = null

    private var sourceUrl = ""
    private var sourceArtistTitle = ""

    val state: StateFlow<PlayerState>
        field = MutableStateFlow<PlayerState>(PlayerState.Default)

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var timerJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        createNotificationChannel()
    }

    override fun onDestroy() {
        serviceScope.cancel()
        releasePlayer()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): Binder {
        sourceUrl = intent?.getStringExtra(KeyConstants.SOURCE_URL) ?: ""
        sourceArtistTitle = intent?.getStringExtra(KeyConstants.SOURCE_ARTIST_TITLE) ?: ""
        initMediaPlayer()

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        cancelNotification()
        return super.onUnbind(intent)
    }

    override fun getPlayerState(): StateFlow<PlayerState> = state

    override fun play() {
        state.update { PlayerState.Playing(getCurrentPosition()) }
        mediaPlayer?.start()
        startTimer()
    }

    override fun pause() {
        mediaPlayer?.pause()
        stopTimer()
        state.update { PlayerState.Paused(getCurrentPosition()) }
    }

    override fun startNotification() {
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createNotification(
                title = getString(R.string.app_name),
                text = sourceArtistTitle
            ),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
    }

    override fun cancelNotification() {
        ServiceCompat.stopForeground(
            this,
            ServiceCompat.STOP_FOREGROUND_REMOVE
        )
    }

    private fun initMediaPlayer() {
        if (sourceUrl.isEmpty()) return

        mediaPlayer?.apply {
            setDataSource(sourceUrl)
            prepareAsync()
            setOnPreparedListener {
                state.update { PlayerState.Prepared }
            }
            setOnCompletionListener {
                stopTimer()
                state.update { PlayerState.Prepared }
                cancelNotification()
            }
        }

    }

    private fun releasePlayer() {
        stopTimer()
        mediaPlayer?.apply {
            stop()
            setOnPreparedListener(null)
            setOnCompletionListener(null)
            release()
        }
        mediaPlayer = null
        state.update { PlayerState.Default }
    }

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = serviceScope.launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(TIMER_DELAY)
                state.update { PlayerState.Playing(getCurrentPosition()) }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun getCurrentPosition(): String = mediaPlayer?.currentPosition?.toMmSs() ?: "00:00"

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(R.string.now_playing),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = getString(R.string.now_playing_description)
        }

        val notificationManager = getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(title: String, text: String): Notification =
        NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

}