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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContent {
            MyTheme {
                MyApp(viewModel)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(viewModel: MainViewModel) {
    val state: Int by viewModel.state.observeAsState(initial = MainViewModel.STATE_STOP)

    Scaffold(
        Modifier
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize(),
        topBar = {
            getTopBar(title = "Countdown Timer")
        },

    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(50.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                getHour(viewModel = viewModel)

                if (state == MainViewModel.STATE_START) {
                    Text(text = ":", fontSize = 20.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.W600)
                }

                getMinute(viewModel = viewModel)
                if (state == MainViewModel.STATE_START) {
                    Text(text = ":", fontSize = 20.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.W600)
                }
                getSecond(viewModel = viewModel)
            }
            Spacer(modifier = Modifier.size(50.dp))
            Button(
                onClick = {
                    if (state == MainViewModel.STATE_START) {
                        viewModel.stop()
                    } else {
                        viewModel.start()
                    }
                }
            ) {
                Text(text = if (state == MainViewModel.STATE_START) "STOP" else "START")
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun getHour(viewModel: MainViewModel) {
    val hour: String by viewModel.showHour.observeAsState("00")
    val state: Int by viewModel.state.observeAsState(initial = MainViewModel.STATE_STOP)
    val transition = updateTransition(targetState = state)

    val textPadding by transition.animateDp() { state ->
        when (state) {
            MainViewModel.STATE_START -> 4.dp
            MainViewModel.STATE_STOP -> 24.dp
            else -> 0.dp
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(targetState = state, animationSpec = tween(500)) { state ->
            if (state == MainViewModel.STATE_STOP) {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_down), "",
                    Modifier
                        .size(40.dp)
                        .rotate(180f)
                        .clickable {
                            viewModel.hourUp()
                        },
                    tint = MaterialTheme.colors.primaryVariant
                )
            } else {
                Text(text = "", Modifier.size(40.dp))
            }
        }

        if (state == MainViewModel.STATE_STOP) {
            Text(text = hour, Modifier.padding(textPadding, 24.dp, textPadding, 24.dp), fontSize = 40.sp, fontWeight = FontWeight.W300, letterSpacing = 10.sp)
        } else {
            Crossfade(targetState = hour) { hour ->
                Text(text = hour, Modifier.padding(textPadding, 24.dp, textPadding, 24.dp), fontSize = 40.sp, fontWeight = FontWeight.W300, letterSpacing = 10.sp)
            }
        }

        Crossfade(targetState = state, animationSpec = tween(500)) { state ->
            if (state == MainViewModel.STATE_STOP) {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_down), "",
                    Modifier
                        .size(40.dp)
                        .clickable {
                            viewModel.hourDown()
                        },
                    tint = MaterialTheme.colors.primaryVariant
                )
            } else {
                Text(text = "", Modifier.size(40.dp))
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun getMinute(viewModel: MainViewModel) {
    val minute: String by viewModel.showMinute.observeAsState("00")
    val state: Int by viewModel.state.observeAsState(initial = MainViewModel.STATE_STOP)
    val transition = updateTransition(targetState = state)

    val textPadding by transition.animateDp() { state ->
        when (state) {
            MainViewModel.STATE_START -> 4.dp
            MainViewModel.STATE_STOP -> 24.dp
            else -> 0.dp
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(targetState = state, animationSpec = tween(500)) { state ->
            if (state == MainViewModel.STATE_STOP) {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_down), "",
                    Modifier
                        .size(40.dp)
                        .rotate(180f)
                        .clickable {
                            viewModel.minuteUp()
                        },
                    tint = MaterialTheme.colors.primaryVariant
                )
            } else {
                Text(text = "", Modifier.size(40.dp))
            }
        }

        if (state == MainViewModel.STATE_STOP) {
            Text(text = minute, Modifier.padding(textPadding, 24.dp, textPadding, 24.dp), fontSize = 40.sp, fontWeight = FontWeight.W300, letterSpacing = 10.sp)
        } else {
            Crossfade(targetState = minute) { minute ->
                Text(text = minute, Modifier.padding(textPadding, 24.dp, textPadding, 24.dp), fontSize = 40.sp, fontWeight = FontWeight.W300, letterSpacing = 10.sp)
            }
        }

        Crossfade(targetState = state, animationSpec = tween(500)) { state ->
            if (state == MainViewModel.STATE_STOP) {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_down), "",
                    Modifier
                        .size(40.dp)
                        .clickable {
                            viewModel.minuteDown()
                        },
                    tint = MaterialTheme.colors.primaryVariant
                )
            } else {
                Text(text = "", Modifier.size(40.dp))
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun getSecond(viewModel: MainViewModel) {
    val second: String by viewModel.showSecond.observeAsState("00")
    val state: Int by viewModel.state.observeAsState(initial = MainViewModel.STATE_STOP)
    val transition = updateTransition(targetState = state)

    val textPadding by transition.animateDp() { state ->
        when (state) {
            MainViewModel.STATE_START -> 4.dp
            MainViewModel.STATE_STOP -> 24.dp
            else -> 0.dp
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(targetState = state, animationSpec = tween(500)) { state ->
            if (state == MainViewModel.STATE_STOP) {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_down), "",
                    Modifier
                        .size(40.dp)
                        .rotate(180f)
                        .clickable {
                            viewModel.secondUp()
                        },
                    tint = MaterialTheme.colors.primaryVariant
                )
            } else {
                Text(text = "", Modifier.size(40.dp))
            }
        }

        if (state == MainViewModel.STATE_STOP) {
            Text(text = second, Modifier.padding(textPadding, 24.dp, textPadding, 24.dp), fontSize = 40.sp, fontWeight = FontWeight.W300, letterSpacing = 10.sp)
        } else {
            Crossfade(targetState = second) { minute ->
                Text(text = minute, Modifier.padding(textPadding, 24.dp, textPadding, 24.dp), fontSize = 40.sp, fontWeight = FontWeight.W300, letterSpacing = 10.sp)
            }
        }

        Crossfade(targetState = state, animationSpec = tween(500)) { state ->
            if (state == MainViewModel.STATE_STOP) {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_down), "",
                    Modifier
                        .size(40.dp)
                        .clickable {
                            viewModel.secondDown()
                        },
                    tint = MaterialTheme.colors.primaryVariant
                )
            } else {
                Text(text = "", Modifier.size(40.dp))
            }
        }
    }
}

@Composable
fun getTopBar(title: String) {
    TopAppBar(
        title = {
            Text(title)
        },
        Modifier.fillMaxWidth()
    )
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp(MainViewModel())
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp(MainViewModel())
    }
}
