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

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.design.snackbar

class DebugObserver(val ctx: Context, val root: View) : LifecycleObserver, AnkoLogger {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun showOnCreate() {

        info("ON_CREATE")

        snackbar(root, "MainActivity created")

        ctx.alert(
                Appcompat,
                title = "Welcome 🤗",
                message = "Make sure to rotate the screen. This dialog only shows in ON_CREATE.",
                init = {
                    okButton { snackbar(root, "👍") }
                    noButton {
                        snackbar(root, "👎")
                        wtf("👎 was selected")
                    }
                }
        ).show()


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun showOnStop() {
        ctx.toast("MainActivity stopped")
    }
}