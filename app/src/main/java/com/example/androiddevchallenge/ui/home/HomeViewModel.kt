package com.example.androiddevchallenge.ui.home

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

class HomeViewModel : ViewModel() {

    data class ViewState(
        val timer: Timer = Timer.Stopped
    )

    var viewState: ViewState by mutableStateOf(ViewState())
        private set

    private var currentTimer: CountDownTimer? = null

    fun start() {
        currentTimer?.cancel()
        currentTimer = object : CountDownTimer(TOTAL_TIME_MILLIS, INTERVAL_TIME_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                viewState = viewState.copy(timer = Timer.Running(millisUntilFinished))
            }

            override fun onFinish() {
                end()
            }
        }.start()
    }

    fun end() {
        currentTimer?.cancel()
        currentTimer = null
        viewState = viewState.copy(timer = Timer.Stopped)
    }

    override fun onCleared() {
        end()
    }

    sealed class Timer {
        data class Running(val remainingTimeMillis: Long) : Timer()
        object Stopped : Timer()
    }
}

private val TOTAL_TIME_MILLIS: Long = TimeUnit.MINUTES.toMillis(1)
private val INTERVAL_TIME_MILLIS: Long = TimeUnit.SECONDS.toMillis(1)
