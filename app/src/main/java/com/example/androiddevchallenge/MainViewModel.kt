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

import android.os.CountDownTimer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    companion object {
        const val STATE_START = 1
        const val STATE_STOP = 2
    }

    private var _state = MutableLiveData<Int>(STATE_STOP)

    var state: LiveData<Int> = _state

    fun start() {
        _state.value = STATE_START
        _startTimer()
    }

    fun stop() {
        _state.value = STATE_STOP
        timer?.cancel()
    }

    private var _hour = MutableLiveData<Int>(0)

    var hour: LiveData<Int> = _hour

    var showHour = Transformations.map(hour) { h ->
        if (h > 9) h.toString() else "0$h"
    }

    private var _minute = MutableLiveData<Int>(0)

    var minute: LiveData<Int> = _minute

    var showMinute = Transformations.map(minute) { m ->
        if (m > 9) m.toString() else "0$m"
    }

    private var _second = MutableLiveData<Int>(0)

    var second: LiveData<Int> = _second

    var showSecond = Transformations.map(second) { s ->
        if (s > 9) s.toString() else "0$s"
    }

    fun hourUp() {
        if (_hour.value != null && _hour.value != 99) _hour.value = _hour.value!! + 1
        else _hour.value = 0
    }

    fun hourDown() {
        if (_hour.value != null && _hour.value != 0) _hour.value = _hour.value!! - 1
        else _hour.value = 99
    }

    fun minuteUp() {
        if (_minute.value != null && _minute.value != 59) _minute.value = _minute.value!! + 1
        else _minute.value = 0
    }

    fun minuteDown() {
        if (_minute.value != null && _minute.value != 0) _minute.value = _minute.value!! - 1
        else _minute.value = 59
    }

    fun secondUp() {
        if (_second.value != null && _second.value != 59) _second.value = _second.value!! + 1
        else _second.value = 0
    }

    fun secondDown() {
        if (_second.value != null && _second.value != 0) _second.value = _second.value!! - 1
        else _second.value = 59
    }

    var timer: CountDownTimer? = null

    private fun _startTimer() {
        timer?.cancel()

        var allSecond = (_second.value ?: 0) + (_minute.value ?: 0) * 60 + (_hour.value ?: 0) * 60 * 60

        timer = object : CountDownTimer(allSecond.times(1000).toLong(), 1000) {

            override fun onTick(p0: Long) {
                if (_second.value == 0 && _minute.value == 0 && _hour.value == 0) {
                    stop()
                } else {
                    if (_second.value != 0) {
                        _second.value = (_second.value ?: 0) - 1
                    } else {
                        _second.value = 59
                        if (_minute.value != 0) {
                            _minute.value = (_minute.value ?: 0) - 1
                        } else {
                            _minute.value = 59
                            if (_hour.value != 0) {
                                _hour.value = (_hour.value ?: 0) - 1
                            }
                        }
                    }
                }
            }

            override fun onFinish() {
                stop()
            }
        }

        timer?.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        timer?.cancel()
    }
}
