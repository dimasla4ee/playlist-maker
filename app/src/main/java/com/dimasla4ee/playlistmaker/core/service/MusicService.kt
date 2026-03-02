package com.dimasla4ee.playlistmaker.core.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.dimasla4ee.playlistmaker.R
import com.dimasla4ee.playlistmaker.core.utils.toMmSs
import com.dimasla4ee.playlistmaker.core.domain.model.PlayerController
import com.dimasla4ee.playlistmaker.core.domain.model.PlayerState
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
    val state: StateFlow<PlayerState>
        field = MutableStateFlow<PlayerState>(PlayerState.Default)
    private var timerJob: Job? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

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

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onDestroy() {
        serviceScope.cancel()
        releasePlayer()
        super.onDestroy()
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
            }
        }

    }

    override fun onBind(intent: Intent?): IBinder {
        sourceUrl = intent?.getStringExtra("song_url") ?: ""
        initMediaPlayer()

        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
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

    fun getCurrentPosition(): String = mediaPlayer?.currentPosition?.toMmSs() ?: "00:00"

    private fun createNotification(): Notification =
        NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Music foreground service")
            .setContentText("Our service is working right now")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()

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

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

}