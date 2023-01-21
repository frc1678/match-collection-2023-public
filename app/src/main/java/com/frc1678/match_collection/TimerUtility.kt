// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.graphics.Color
import android.os.CountDownTimer
import android.widget.Button
import android.widget.LinearLayout
import kotlin.math.roundToInt

// Class for all timer functions.
class TimerUtility {
    // Create a thread for the match timer to run asynchronously while the app runs other tasks.
    class MatchTimerThread : Thread() {
        var time = 0f

        // Return stage to be displayed on timer.
        private fun stage(time: Int): Constants.Stage {
            return if (time >= 135) {
                Constants.Stage.AUTO
            } else {
                Constants.Stage.TELEOP
            }
        }

        // Begins CountDownTimer when timer button is clicked.
        private fun run(
            context: CollectionActivity,
            btn_timer: Button,
            btn_proceed: Button,
            layout: LinearLayout
        ) {
            is_match_time_ended = false
            // Create a CountDownTimer that will count down in by seconds starting from 150 seconds.
            match_timer = object : CountDownTimer(15000, 1000) {
                // Executes tasks every second.
                override fun onTick(millisUntilFinished: Long) {
                    time = millisUntilFinished / 1000f

                    // Display stage and time on timer button.
                    if (context is CollectionObjectiveActivity) {
                        btn_timer.text = context.getString(
                            R.string.tv_time_display,
                            stage(time.roundToInt()),
                            time.roundToInt().toString()
                        )
                    } else {
                        btn_timer.text = time.roundToInt().toString()
                    }
                    // Convert time to a three-digit string to be recorded in timeline.
                    match_time = (time - 1).toInt().toString().padStart(3, '0')

                    if (context is CollectionObjectiveActivity) {
                        if (!is_teleop_activated and (time.roundToInt() <= 135)) {
                            layout.setBackgroundColor(Color.RED)
                        } else {
                            layout.setBackgroundColor(Color.WHITE)
                        }
                    }
                }

                // Display 0 and change button states when countdown finishes.
                override fun onFinish() {
                    if (context is CollectionObjectiveActivity) {
                        btn_timer.text = context.getString(
                            R.string.tv_time_display,
                            stage(time.roundToInt()),
                            context.getString(R.string.final_time)
                        )
                    } else {
                        btn_timer.text = "0"
                    }
                    btn_timer.isEnabled = false
                    if (context is CollectionObjectiveActivity) {
                        context.endAction()
                        context.enableButtons()
                    }
                    btn_proceed.text = context.getString(R.string.btn_proceed)
                    is_match_time_ended = true
                    btn_proceed.isEnabled = true
                    is_teleop_activated = true
                }
            }.start()
        }

        // Initialize timer, called in CollectionObjectiveActivity.kt.
        fun initTimer(
            context: CollectionActivity,
            btn_timer: Button,
            btn_proceed: Button,
            layout: LinearLayout
        ) {
            run(
                context = context,
                btn_timer = btn_timer,
                btn_proceed = btn_proceed,
                layout = layout
            )
        }
    }
}

