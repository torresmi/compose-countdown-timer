/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.home

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
        currentTimer = object : CountDownTimer(TOTAL_TOAST_TIME_MILLIS, INTERVAL_TIME_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                viewState = viewState.copy(timer = Timer.Running(millisUntilFinished))
            }

            override fun onFinish() {
                viewState = viewState.copy(timer = Timer.Finished)
                currentTimer = null
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
        class Running(val remainingTimeMillis: Long) : Timer()
        object Stopped : Timer()
        object Finished : Timer()
    }
}

private val TOTAL_TOAST_TIME_MILLIS: Long = TimeUnit.SECONDS.toMillis(6)
private val INTERVAL_TIME_MILLIS: Long = TimeUnit.SECONDS.toMillis(1)
