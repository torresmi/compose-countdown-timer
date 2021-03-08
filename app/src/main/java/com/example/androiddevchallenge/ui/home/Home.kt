package com.example.androiddevchallenge.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.theme.MyTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = HomeViewModel()) {
    Home(
        viewState = homeViewModel.viewState,
        onTimeStart = homeViewModel::start,
        onTimeCancel = homeViewModel::end,
    )
}

private const val TIME_FORMAT = "m:s"

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
                backgroundColor = MaterialTheme.colors.primary
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
    val text = when (timer) {
        is HomeViewModel.Timer.Running -> {
            val remainingTime = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
                .format(Date(timer.remainingTimeMillis))
            stringResource(id = R.string.home_timer, remainingTime)
        }
        is HomeViewModel.Timer.Stopped -> stringResource(id = R.string.home_start)
    }

    Text(text = text)
}

@Composable
private fun TimerFAB(
    timer: HomeViewModel.Timer,
    onTimeStart: () -> Unit,
    onTimeCancel: () -> Unit,
) {
    val (icon, action) = when (timer) {
        is HomeViewModel.Timer.Running -> Icons.Default.Stop to onTimeCancel
        is HomeViewModel.Timer.Stopped -> Icons.Default.Timer to onTimeStart
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
