package com.dimasla4ee.playlistmaker.feature.player.presentation.viewmodel

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dimasla4ee.playlistmaker.core.presentation.util.toMmSs

class MediaPlayerViewModel(
    private val sourceUrl: String
) : ViewModel() {

    private var mediaPlayer = MediaPlayer()
    private var handler = Handler(Looper.getMainLooper())

    private val _state = MutableLiveData(State.DEFAULT)
    val state: LiveData<State>
        get() = _state

    private val _timer = MutableLiveData(0.toMmSs())
    val timer: LiveData<String>
        get() = _timer

    private val updateTimerRunnable = Runnable {
        if (_state.value == State.PLAYING) {
            _timer.postValue(mediaPlayer.currentPosition.toMmSs())
            startTimer()
        }
    }

    init {
        preparePlayer()
    }

    private fun preparePlayer() {
        with(mediaPlayer) {
            setDataSource(sourceUrl)
            prepareAsync()
            setOnPreparedListener {
                _state.postValue(State.PREPARED)
            }
            setOnCompletionListener {
                resetTimer()
                _state.postValue(State.PREPARED)
            }
        }
    }

    private fun startPlayback() {
        startTimer()
        mediaPlayer.start()
        _state.postValue(State.PLAYING)
    }

    private fun pausePlayback() {
        pauseTimer()
        mediaPlayer.pause()
        _state.postValue(State.PAUSED)
    }

    fun onPlayButtonClicked() {
        when (_state.value) {
            State.PLAYING -> {
                pausePlayback()
            }

            State.PREPARED, State.PAUSED -> {
                startPlayback()
            }

            else -> Unit
        }
    }

    private fun startTimer() {
        handler.postDelayed(updateTimerRunnable, DELAY)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(updateTimerRunnable)
    }

    private fun resetTimer() {
        pauseTimer()
        _timer.postValue(0.toMmSs())
    }

    override fun onCleared() {
        super.onCleared()
        pauseTimer()
        mediaPlayer.release()
    }

    enum class State {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }

    companion object {

        private const val DELAY = 300L
    }
}