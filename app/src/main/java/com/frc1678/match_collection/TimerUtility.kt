// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.climb_popup.view.*
import kotlin.math.roundToInt

// Class for all timer functions.
class TimerUtility {
    // Create a thread for the match timer to run asynchronously while the app runs other tasks.
    class MatchTimerThread : Thread() {
        var time = 0f

        // Return stage to be displayed on timer.
        private fun returnStage(time: Int): Constants.Stage {
            return if (time >= 135) {
                Constants.Stage.AUTO
            } else {
                Constants.Stage.TELEOP
            }
        }

        // Begins CountDownTimer when timer button is clicked.
        private fun run(
            context: Context,
            btn_timer: Button,
            btn_proceed: Button,
            layout: LinearLayout
        ) {
            // Create a CountDownTimer that will count down in by seconds starting from 150 seconds.
            match_timer = object : CountDownTimer(20000, 1000) {
                // Executes tasks every second.
                override fun onTick(millisUntilFinished: Long) {
                    time = millisUntilFinished / 1000f

                    // Display stage and time on timer button.
                    btn_timer.text = context.getString(
                        R.string.tv_time_display,
                        returnStage(time.roundToInt()),
                        time.roundToInt().toString()
                    )
                    // Convert time to a three-digit string to be recorded in timeline.
                    match_time = (time - 1).toInt().toString().padStart(3, '0')

                    if (!is_teleop_activated and (time.roundToInt() == 135)) {
                        layout.setBackgroundColor(Color.RED)
                    }
                }
                // Display 0 and change button states when countdown finishes.
                override fun onFinish() {
                    if (!climb_timer_paused && climb_timer != null) { // timer is currently running
                        climb_timer!!.onFinish()
                        climb_end_time = match_time}
                    btn_timer.text = context.getString(
                        R.string.tv_time_display,
                        returnStage(time.roundToInt()),
                        context.getString(R.string.final_time)
                    )
                    btn_timer.isEnabled = false
                    btn_proceed.text = context.getString(R.string.btn_proceed)
                    is_match_time_ended = true
                    btn_proceed.isEnabled = true
                    is_teleop_activated = true
                }
            }.start()
        }

        // Initialize timer, called in CollectionObjectiveActivity.kt.
        fun initTimer(
            context: Context,
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

    /** A new thread for the climb timer in the climb popup. The timer starts from 0 seconds and
     * counts up until it is canceled or reaches 2 minutes and 15 seconds. This uses the
     * [CountDownTimer] class, possibly in an unintended way, but it works. */
    class ClimbTimerThread(val context: Context, val view: View) : Thread() {
        var time = 0
        init { startTimer() }

        private fun startTimer() {
            climb_timer = object : CountDownTimer(135_000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    time = (135f - (millisUntilFinished / 1000f)).toInt() + 1
                    view.btn_climb_timer.text = context.getString(R.string.climb_timer, (time + climb_time).toString())
                }

                override fun onFinish() {
                    super.cancel()
                    climb_timer_paused = true
                    climb_time += time
                    view.btn_climb_timer.text = context.getString(R.string.climb_timer_done, climb_time.toString())
                    view.btn_climb_done.isEnabled = true

                }

            }.start()
        }
    }
}
