// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.climb_popup.*
import kotlinx.android.synthetic.main.climb_popup.view.*
import kotlinx.android.synthetic.main.collection_objective_activity.*
import kotlinx.android.synthetic.main.error_pop_up.view.*
import java.lang.Integer.parseInt

// Activity for Objective Match Collection to scout the objective gameplay of a single team in a match.
class CollectionObjectiveActivity : CollectionActivity() {
    private var numActionOne = 0 //SCORE_BALL_LOW
    private var numActionTwo = 0 //SCORE_BALL_HIGH_HUB
    private var numActionThree = 0 //SCORE_BALL_HIGH_LAUNCHPAD
    private var numActionFour = 0 //SCORE_BALL_HIGH_OTHER
    private var numActionFive = 0 //NUMBER OF INTAKES
    private var numActionSix = 0 //CATCH_CARGO
    private var numActionSeven = 0 //SCORE_OPPOSING_BALL

    private var isTimerRunning = false
    //FALSE = LOW
    private var removedTimelineActions: ArrayList<HashMap<String, String>> = ArrayList()

    // Set timer to start match when timer is started or reset.
    private fun timerReset() {
        match_timer?.cancel()
        match_timer = null
        timeline.clear()
        removedTimelineActions.clear()
        btn_timer.text = getString(R.string.btn_timer_start)
    }

    // Add performed action to timeline, including action type and time of action.
    private fun timelineAdd(match_time: String, action_type: Constants.ActionType) {
        val actionHashMap: HashMap<String, String> = hashMapOf(
            Pair("match_time", match_time),
            Pair("action_type", "$action_type")
        )
        timeline.add(actionHashMap)
        removedTimelineActions.clear()

        enableButtons()
    }

    // If stage and time contradict when action is recorded, add action to timeline with time value
    // dictated by stage.
    private fun timelineAddWithStage(action_type: Constants.ActionType) {
        if (!is_teleop_activated and (parseInt(match_time) < parseInt(getString(R.string.final_auto_time)))) {
            timelineAdd(match_time = getString(R.string.final_auto_time), action_type = action_type)
        } else if (is_teleop_activated and (parseInt(match_time) > parseInt(getString(R.string.initial_teleop_time)))) {
            timelineAdd(
                match_time = getString(R.string.initial_teleop_time),
                action_type = action_type
            )
        } else {
            timelineAdd(match_time = match_time, action_type = action_type)
        }
    }

    // Remove previously inputted action from timeline.
    private fun timelineRemove() {
        var removeOneMore = false
        // Decrement action values displayed on action counters.
        when (timeline[timeline.size - 1]["action_type"].toString()) {
            Constants.ActionType.SCORE_BALL_LOW.toString() -> {
                numActionOne--
                setCounterTexts()
            }

            Constants.ActionType.SCORE_BALL_HIGH_HUB.toString() -> {
                numActionTwo--
                setCounterTexts()
            }

            Constants.ActionType.SCORE_BALL_HIGH_LAUNCHPAD.toString() -> {
                numActionThree--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_HIGH_OTHER.toString() -> {
                numActionFour--
                setCounterTexts()
            }
            Constants.ActionType.INTAKE.toString() -> {
                numActionFive--
                setCounterTexts()
            }

            Constants.ActionType.CATCH_EXIT_BALL.toString() -> {
                numActionSix--
                showErrorPopup(btn_error)
            }
            Constants.ActionType.SCORE_OPPONENT_BALL.toString() -> {
                numActionSeven--
                showErrorPopup(btn_error)
            }
            Constants.ActionType.END_CLIMB.toString() -> {
                removeOneMore = true
                climb_timer_paused = false
                climb_timer = null
            }
            Constants.ActionType.START_INCAP.toString() -> {
                tb_action_one.isChecked = false
            }
            Constants.ActionType.END_INCAP.toString() -> {
                tb_action_one.isChecked = true
            }
        }

        // Add removed action to removedTimelineActions so it can be redone if needed.
        removedTimelineActions.add(timeline[timeline.size - 1])

        // Remove most recent timeline entry.
        timeline.removeAt(timeline.size - 1)

        enableButtons()

        if (removeOneMore) timelineRemove()
    }

    // Pull from removedTimelineActions to redo timeline actions after undo.
    private fun timelineReplace() {
        var replaceOneMore = false

        // Add most recently undone action from removedTimelineActions back to timeline.
        timeline.add(removedTimelineActions[removedTimelineActions.size - 1])

        // Increment action values and display on action counters if re-adding a counter action from the timeline.
        when (removedTimelineActions[removedTimelineActions.size - 1]["action_type"].toString()) {
            Constants.ActionType.SCORE_BALL_LOW.toString() -> {
                numActionOne++
                setCounterTexts()
            }

            Constants.ActionType.SCORE_BALL_HIGH_HUB.toString() -> {
                numActionTwo++
                setCounterTexts()
            }

            Constants.ActionType.SCORE_BALL_HIGH_LAUNCHPAD.toString() -> {
                numActionThree++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_HIGH_OTHER.toString() -> {
                numActionFour++
                setCounterTexts()
            }
            Constants.ActionType.INTAKE.toString() -> {
                numActionFive++
                setCounterTexts()
            }

            Constants.ActionType.CATCH_EXIT_BALL.toString() -> {
                numActionSix++
                showErrorPopup(btn_error)
            }
            Constants.ActionType.SCORE_OPPONENT_BALL.toString() -> {
                numActionSeven++
                showErrorPopup(btn_error)
            }
            Constants.ActionType.START_CLIMB.toString() -> {
                replaceOneMore = true
                climb_timer_paused = true
                climb_timer = null
            }
            Constants.ActionType.START_INCAP.toString() -> {
                tb_action_one.isChecked = true
            }
            Constants.ActionType.END_INCAP.toString() -> {
                tb_action_one.isChecked = false
            }
        }

        // Remove the redone action from removedTimelineActions.
        removedTimelineActions.removeAt(removedTimelineActions.size - 1)

        enableButtons()

        if (replaceOneMore) timelineReplace()
    }

    // Enable and disable buttons based on actions in timeline and timer stage.
    fun enableButtons()
    {
        val isIncap = tb_action_one.isChecked
        // Enable and disable buttons based on values of condition booleans defined previously.
        btn_action_one.isEnabled = !(!isTimerRunning or popup_open or isIncap)
        btn_action_two.isEnabled = !(!isTimerRunning or popup_open or isIncap)
        btn_action_five.isEnabled = !(!isTimerRunning or popup_open or isIncap)

        btn_error.isEnabled = !(!isTimerRunning or popup_open)

        tb_action_one.isEnabled = !(!is_teleop_activated or popup_open)

        btn_climb.isEnabled = !(!is_teleop_activated or popup_open or isIncap or climb_timer_paused)
        btn_climb.text =
            if (climb_timer_paused) getString(R.string.btn_climbed)
            else getString(R.string.btn_climb)

        btn_undo.isEnabled = (timeline.size > 0) and !popup_open
        btn_redo.isEnabled = (removedTimelineActions.size > 0) and !popup_open

        btn_timer.isEnabled = !((timeline.size > 0) or is_teleop_activated or popup_open)
        btn_proceed_edit.isEnabled = ((!is_teleop_activated) or (is_match_time_ended)) and !popup_open
    }

    // Function to end incap if still activated at end of the match.
    fun endAction() {
        if (tb_action_one.isChecked) {
            tb_action_one.isChecked = false
            timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
        }
    }

    // Set high and low goal counter values.
    private fun setCounterTexts() {
        btn_action_one.text = getString(R.string.btn_action_one, numActionOne.toString())
        btn_action_two.text = getString(R.string.btn_action_two, numActionTwo.toString())
        btn_action_five.text = getString(R.string.btn_action_five, numActionFive.toString())
    }

    // Initialize button and toggle button onClickListeners.
    private fun initOnClicks() {
        btn_proceed_edit.setOnClickListener {
            if (!is_teleop_activated) {
                is_teleop_activated = true
                enableButtons()
                btn_proceed_edit.text = getString(R.string.btn_proceed)
                btn_proceed_edit.isEnabled = false
                btn_timer.isEnabled = false
                objective_match_collection_layout.setBackgroundColor(Color.WHITE)
            } else {
                endAction()
                val intent = Intent(this, MatchInformationEditActivity::class.java)
                startActivity(
                    intent, ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        btn_proceed_edit, "proceed_button"
                    ).toBundle()
                )
            }
        }

        // Start timer on normal click if timer is not running.
        btn_timer.setOnClickListener {
            if (!isTimerRunning) {
                TimerUtility.MatchTimerThread()
                    .initTimer(
                        context = this,
                        btn_timer = btn_timer,
                        btn_proceed = btn_proceed_edit,
                        btn_incap = tb_action_one,
                        layout = objective_match_collection_layout
                    )
                isTimerRunning = true
                enableButtons()
                btn_proceed_edit.isEnabled = true
            }
        }

        // Reset timer on long click if timer is running.
        btn_timer.setOnLongClickListener(View.OnLongClickListener {
            if (isTimerRunning and !is_teleop_activated) {
                timerReset()
                timeline = ArrayList()
                isTimerRunning = false
                is_teleop_activated = false
                enableButtons()
                btn_proceed_edit.isEnabled = false
                btn_proceed_edit.text = getString(R.string.btn_to_teleop)
                objective_match_collection_layout.setBackgroundColor(Color.WHITE)
            }
            return@OnLongClickListener true
        })

        // Increment button action one by one when clicked and add action to timeline.
        btn_action_one.setOnClickListener {
            //FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_LOW)
            numActionOne++
            setCounterTexts()
        }

        // Increment button action two by one when clicked and add action to timeline.
        btn_action_two.setOnClickListener {
            //FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_HIGH_HUB)
            numActionTwo++
            setCounterTexts()
        }

        // Increment button action three by one when clicked and add action to timeline.


        // Increment button action five by one when clicked and add action to timeline.
        btn_action_five.setOnClickListener {
            timelineAddWithStage(action_type = Constants.ActionType.INTAKE)
            numActionFive++
            setCounterTexts()
        }


        // Start incap if clicking the incap toggle button checks the toggle button.
        // Otherwise, end incap.
        tb_action_one.setOnClickListener {
            if (tb_action_one.isChecked) {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.START_INCAP)
            } else {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
            }
        }

        // Open the Climb popup window.
        btn_climb.setOnClickListener {
            val popupView = View.inflate(this, R.layout.climb_popup, null)
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val popupWindow = PopupWindow(popupView, width, height, false)
            popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)
            popup_open = true
            climb_time = 0
            enableButtons()
            popupView.btn_climb_done.isEnabled = false
            popupView.btn_climb_timer.setOnClickListener {
                if (climb_timer == null) { // timer hasn't started
                    TimerUtility.ClimbTimerThread(this, popupView)
                    climb_start_time = match_time
                } else if (!climb_timer_paused) { // timer is currently running
                    climb_timer!!.onFinish()
                    climb_end_time = match_time
                } else { // timer is paused
                    TimerUtility.ClimbTimerThread(this, popupView)
                    climb_timer_paused = false
                    popupView.btn_climb_done.isEnabled = false
                }
            }
            popupView.btn_climb_cancel.setOnClickListener {
                if (climb_timer != null) {
                    climb_timer!!.onFinish()
                    climb_timer = null
                }

              climb_time = 0
                climb_timer_paused = false
                climb_level = Constants.ClimbLevel.NONE
                climb_start_time = null
                climb_end_time = null
                popupWindow.dismiss()
                popup_open = false
                enableButtons()
            }
            popupView.btn_climb_done.setOnClickListener {
                popupWindow.dismiss()
                btn_climb.isEnabled = false
                popup_open = false
                climb_start_time?.let { it1 -> timelineAdd(it1, Constants.ActionType.START_CLIMB) }
                climb_end_time?.let { it1 -> timelineAdd(it1, Constants.ActionType.END_CLIMB) }
                enableButtons()
            }
            popupView.btn_climb_lv0.isActivated = true
            climb_level = Constants.ClimbLevel.NONE
            popupView.btn_climb_lv0.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = true
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.NONE
            }
            popupView.btn_climb_lv1.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = true
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.LOW
            }
            popupView.btn_climb_lv2.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = true
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.MID
            }
            popupView.btn_climb_lv3.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = true
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.HIGH
            }
            popupView.btn_climb_lv4.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = true
                climb_level = Constants.ClimbLevel.TRAVERSAL
            }
        }

        // Remove previous action from timeline when undo button is clicked.
        btn_undo.setOnClickListener {
            timelineRemove()
        }

        // Replace previously undone action to timeline when redo button is clicked.
        btn_redo.setOnClickListener {
            timelineReplace()
        }

        btn_error.setOnClickListener {
            showErrorPopup(it)
        }
    }

    private fun showErrorPopup(view: View) {
        popup_open = true
        enableButtons()

        // Inflate a custom view using layout inflater
        val popupView = View.inflate(this, R.layout.error_pop_up,null)
        popupView.catch_cargo.text = getString(R.string.btn_action_six, numActionSix.toString())
        popupView.score_opp.text = getString(R.string.btn_action_seven, numActionSeven.toString())

        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
            popupView, // Custom view to show in popup window
            LinearLayout.LayoutParams.MATCH_PARENT, // Width of popup window
            600, // Window height
            true
        )
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        popupView.catch_cargo.setOnClickListener {
            numActionSix++
            timelineAdd(match_time = match_time, action_type = Constants.ActionType.CATCH_EXIT_BALL)
            popupWindow.dismiss()
            popup_open = false
            enableButtons()
        }
        popupView.score_opp.setOnClickListener {
            numActionSeven++
            timelineAdd(match_time = match_time, action_type = Constants.ActionType.SCORE_OPPONENT_BALL)
            popupWindow.dismiss()
            popup_open = false
            enableButtons()
        }
        popupWindow.setOnDismissListener {
            popup_open = false
            enableButtons()
        }
    }

    // Set team number view to team number defined in References.kt and set team number to alliance color.
    private fun initTeamNum() {
        tv_team_number.text = team_number
        if (alliance_color == Constants.AllianceColor.RED) {
            tv_team_number.setTextColor(resources.getColor(R.color.alliance_red_light, null))
        } else if (alliance_color == Constants.AllianceColor.BLUE) {
            tv_team_number.setTextColor(resources.getColor(R.color.alliance_blue_light, null))
        }
    }

    // Begin intent used in onKeyLongPress to restart app from StartingPositionObjectiveActivity.kt.
    private fun intentToPreviousActivity() {
        is_teleop_activated = false
        startActivity(
            Intent(this, StartingPositionObjectiveActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    // Restart app from StartingPositionObjectiveActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> intentToPreviousActivity() }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_objective_activity)

        timerReset()
        setCounterTexts()
        initOnClicks()
        initTeamNum()
    }
}
