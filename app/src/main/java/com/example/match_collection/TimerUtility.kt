package com.example.match_collection

import android.os.CountDownTimer
import android.widget.Button
import kotlin.math.roundToInt

class TimerUtility {
    // Creates a class for all timer functions
    class MatchTimerThread : Thread() {
        var time = 0f
        // Creates a thread for the match timer to run while the app runs other tasks
        fun run(btn_timer: Button, btn_proceed: Button) {
            match_timer = object : CountDownTimer(150000, 1000) {
                // Creates a CountDownTimer that will count in intervals of 1000 milliseconds from 150000 milliseconds
                override fun onTick(millisUntilFinished: Long) {
                    // Sets millisUntilFinished as a Long
                    time = millisUntilFinished / 1000f
                    btn_timer.text = "${ObjectiveMatchCollectionActivity().returnStage(time.roundToInt())} - ${time.roundToInt()}"
                    // The time variable is millisUntilFinished converted into seconds
                    match_time = (time - 1).toInt().toString().padStart(3, '0')
                }
                override fun onFinish() {
                    btn_timer.setText("${ObjectiveMatchCollectionActivity().returnStage(time.roundToInt())} - 0")
                    btn_timer.isEnabled = false
                    btn_proceed.isEnabled = true
                }
            // Starts the thread
            }.start()
        }

        // Start timer
        fun initTimer(btn_timer: Button, btn_proceed: Button) {
            run(btn_timer, btn_proceed)
        }
    }
}
