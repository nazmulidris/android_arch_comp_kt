/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package arch_comp_kt.nazmul.com

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Looper
import android.widget.Toast
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.text.DateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

// ViewModel
class StateViewModel(context: Application) : AndroidViewModel(context), AnkoLogger {
    val mExectuor: ScheduledExecutorService
    val mCounter = Counter()

    init {
        mExectuor = Executors.newSingleThreadScheduledExecutor()
        mExectuor.scheduleWithFixedDelay(
                { recurringTask() },
                0,
                1,
                TimeUnit.SECONDS)
        info("ViewModel constructor: created executor")
    }

    // Data
    private lateinit var _data: Data
    var mData: Data
        get() {
            if (::_data.isInitialized) {
                Toast.makeText(getApplication(), "Re-using ViewModel", Toast.LENGTH_SHORT).show()
            } else {
                _data = Data(Random().nextInt(1000).toString(), Date())
                Toast.makeText(getApplication(), "This is a new ViewModel", Toast.LENGTH_SHORT).show()
            }
            return _data
        }
        set(mData) {
            _data = mData
        }

    // Counter
    fun recurringTask() {
        info(if (mCounter.count % 2 == 0L) "task: tick" else "task: tock")
        mCounter.increment()
    }

    override fun onCleared() {
        super.onCleared()
        mExectuor.shutdown()
        info(mCounter.toString())
        info("onCleared: lifecycle of activity finished")
    }

}

// Data
data class Data(val id: String, val time: Date) :
        MutableLiveData<Data>() {

    private var _clicked: Boolean = false
    var clicked: Boolean
        get() {
            return _clicked
        }
        set(value) {
            _clicked = value
            setValue(this)
        }

    override fun toString(): String {
        val format = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US)
        return "id: $id, " +
                "clickedOk/Cancel: ${if (clicked) "✔︎" else "✖︎"}, " +
                "created: ${format.format(time)}"
    }
}

// Counter
class Counter(private var value: Long = 0L) : MutableLiveData<Long>(), AnkoLogger {

    init {
        setValue(value)
    }

    var count: Long
        get() {
            return value
        }
        set(value) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                // UI thread
                setValue(value)
            } else {
                // Non UI thread
                postValue(value)
            }
        }

    fun increment() {
        count = value++
    }

    override fun toString(): String = "count = $value"
}