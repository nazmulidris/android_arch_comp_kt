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

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.design.snackbar

class MainActivity : AppCompatActivity(), AnkoLogger {

    lateinit var mStateViewModel: StateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Kotlin goodness with view binding
        viewBinding()

        // set the rootView
        val rootView: View = findViewById(android.R.id.content)

        // Lifecycle awareness
        lifecycle.addObserver(DebugObserver(this, rootView))
        lifecycle.addObserver(FontObserver(this, toolbar))

        // ModelView
        setupModelView()

        // LiveData
        attachLiveDataObservers()

        // show dialog on first run
        showDialog(rootView = rootView)
    }

    // No need to use findViewById() anymore!
    fun viewBinding() {
        // Use synthetic properties on views!
        data_textview.text = "Data!!!"
        counter_textview.text = "Counter!!!"
    }

    fun setupModelView() {
        mStateViewModel = ViewModelProviders.of(this).get(StateViewModel::class.java)
        data_textview.text = mStateViewModel.mData.toString()
    }

    private fun attachLiveDataObservers() {
        mStateViewModel.mCounter.observe(
                this,
                Observer {
                    counter_textview.text = "Count: ${it.toString()}"
                })
        mStateViewModel.mData.observe(
                this,
                Observer {
                    data_textview.text = it.toString()
                }
        )
    }

    fun showDialog(rootView: View) {
        if (::mStateViewModel.isInitialized)
            if (!mStateViewModel.mData.clicked)
                alert(
                        Appcompat,
                        title = "Welcome 🤗",
                        message = "Make sure to rotate the screen. This dialog only shows in ON_CREATE.",
                        init = {
                            okButton {
                                mStateViewModel.mData.clicked = true
                                snackbar(rootView, "👍")
                                info("👍 was selected")
                            }
                            noButton {
                                mStateViewModel.mData.clicked = true
                                snackbar(rootView, "👎")
                                info("👎 was selected")
                            }
                            onCancelled {
                                mStateViewModel.mData.clicked = true
                                snackbar(rootView, "👊")
                                info("👊 dialog was dismissed")
                            }
                        }
                ).show()
    }

}

