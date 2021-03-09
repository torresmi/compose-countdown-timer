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

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.amber100
import com.example.androiddevchallenge.ui.theme.toastBrown
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TIME_FORMAT = "ss"

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = HomeViewModel()) {
    Home(
        viewState = homeViewModel.viewState,
        onTimeStart = homeViewModel::start,
        onTimeCancel = homeViewModel::end,
    )
}

@Composable
fun Home(
    viewState: HomeViewModel.ViewState,
    onTimeStart: () -> Unit,
    onTimeCancel: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.home_title)) },
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.primary,
            )
        },
        floatingActionButton = {
            TimerFAB(viewState.timer, onTimeStart, onTimeCancel)
        },
    ) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(MaterialTheme.colors.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                TimerDisplay(viewState.timer)
            }
        }
    }
}

@Composable
private fun TimerDisplay(timer: HomeViewModel.Timer) {
    when (timer) {
        is HomeViewModel.Timer.Running -> {
            TimerRun(timer)
        }
        is HomeViewModel.Timer.Stopped -> {
            TimerStop()
        }
        is HomeViewModel.Timer.Finished -> {
            TimerFinish()
        }
    }
}

@Composable
private fun TimerRun(timer: HomeViewModel.Timer.Running) {
    val remainingTime = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        .format(Date(timer.remainingTimeMillis))
    val text = stringResource(id = R.string.home_timer, remainingTime)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = text, style = MaterialTheme.typography.h1)

        val breadColor = remember { Animatable(amber100) }
        LaunchedEffect(key1 = timer.remainingTimeMillis) {
            breadColor.animateTo(
                toastBrown,
                animationSpec = tween(
                    durationMillis = timer.remainingTimeMillis.toInt(),
                    easing = LinearEasing,
                ),
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_toast),
            contentScale = ContentScale.FillWidth,
            contentDescription = null,
            colorFilter = ColorFilter.tint(breadColor.value, BlendMode.DstAtop),
            modifier = Modifier.width(235.dp),
        )
    }
}

@Composable
private fun TimerStop() {
    val text = stringResource(id = R.string.home_start)

    Text(text = text, style = MaterialTheme.typography.h4)
}

@Composable
private fun TimerFinish() {
    val infiniteTransition = rememberInfiniteTransition()
    val jump by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20.dp.value,
        animationSpec = infiniteRepeatable(
            animation = tween(750),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    Image(
        painter = painterResource(id = R.drawable.finished),
        contentDescription = null,
        modifier = Modifier.offset(y = jump.dp),
    )
}

@Composable
private fun TimerFAB(
    timer: HomeViewModel.Timer,
    onTimeStart: () -> Unit,
    onTimeCancel: () -> Unit,
) {
    val (icon, action) = when (timer) {
        is HomeViewModel.Timer.Running -> Icons.Default.Stop to onTimeCancel
        is HomeViewModel.Timer.Stopped,
        is HomeViewModel.Timer.Finished -> Icons.Default.Timer to onTimeStart
    }

    FloatingActionButton(onClick = action) {
        Icon(imageVector = icon, contentDescription = null)
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
private fun LightPreview() {
    MyTheme {
        HomeScreen()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
private fun DarkPreview() {
    MyTheme(darkTheme = true) {
        HomeScreen()
    }
}
