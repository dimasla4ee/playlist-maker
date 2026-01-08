package com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasla4ee.playlistmaker.core.utils.toMmSs
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    private val sourceUrl: String,
    private val mediaPlayer: MediaPlayer
) : ViewModel() {

    val state: StateFlow<State>
        field = MutableStateFlow<State>(State.Default)

    private var timerJob: Job? = null

    init {
        preparePlayer()
    }

    private fun preparePlayer() {
        with(mediaPlayer) {
            setDataSource(sourceUrl)
            prepareAsync()
            setOnPreparedListener {
                state.update { State.Prepared }
            }
            setOnCompletionListener {
                timerJob?.cancel()
                state.update { State.Prepared }
            }
        }
    }

    private fun startPlayback() {
        mediaPlayer.start()
        state.update { State.Playing(getCurrentPosition()) }
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(DELAY)
                state.update { State.Playing(getCurrentPosition()) }
            }
        }
    }

    private fun pausePlayback() {
        mediaPlayer.pause()
        timerJob?.cancel()
        state.update { State.Paused(getCurrentPosition()) }
    }

    fun onPlayButtonClicked() {
        when (state.value) {
            is State.Playing -> {
                pausePlayback()
            }

            State.Prepared, is State.Paused -> {
                startPlayback()
            }

            else -> Unit
        }
    }

    fun releasePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        state.update { State.Default }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onPause() {
        pausePlayback()
    }

    private fun getCurrentPosition(): String = mediaPlayer.currentPosition.toMmSs()

    sealed class State(val isPlayButtonEnabled: Boolean, val progress: String) {

        object Default : State(isPlayButtonEnabled = false, progress = "00:00")
        object Prepared : State(isPlayButtonEnabled = true, progress = "00:00")
        class Playing(progress: String) : State(isPlayButtonEnabled = true, progress)
        class Paused(progress: String) : State(isPlayButtonEnabled = true, progress)

    }

    companion object {
        private const val DELAY = 300L
    }

}