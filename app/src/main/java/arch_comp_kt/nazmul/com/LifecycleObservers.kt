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
import android.graphics.Typeface
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.provider.FontRequest
import android.support.v4.provider.FontsContractCompat
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.design.snackbar

class DebugObserver(val ctx: Context, val root: View) : LifecycleObserver, AnkoLogger {
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun showOnCreate() {
        info("ON_CREATE")
        snackbar(root, "MainActivity created")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun showOnStop() {
        ctx.toast("MainActivity stopped")
    }
}

class FontObserver(val mContext: Context, val mToolbar: Toolbar) : LifecycleObserver, AnkoLogger {

    // Lazy create this property, since it's heavy
    val mFontHandler: Handler by lazy {
        with(HandlerThread("fonts")) {
            start()
            Handler(looper)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun downloadFont() {
        info("${::downloadFont.name}(): Running")

        //val query = "name=Open Sans&weight=800&italic=0"
        val query = "name=Noto Sans&weight=700&italic=1"

        // Start async fetch on the handler thread
        FontsContractCompat.requestFont(
                mContext,
                FontRequest(
                        "com.google.android.gms.fonts",
                        "com.google.android.gms",
                        query,
                        R.array.com_google_android_gms_fonts_certs),
                object : FontsContractCompat.FontRequestCallback() {
                    override fun onTypefaceRetrieved(typeface: Typeface) {
                        // If we got our font apply it to the toolbar
                        styleToolbar(typeface)
                    }

                    override fun onTypefaceRequestFailed(reason: Int) {
                        error("Failed to fetch Toolbar font: $reason")
                    }
                },
                mFontHandler)
    }

    fun styleToolbar(typeface: Typeface) {
        mToolbar.applyRecursively {
            when (it) {
                is TextView -> it.typeface = typeface
            }
        }
    }
}