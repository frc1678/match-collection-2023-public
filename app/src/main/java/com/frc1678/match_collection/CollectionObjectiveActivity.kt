// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.climb_popup.view.*
import kotlinx.android.synthetic.main.collection_objective_activity.*
import java.lang.Integer.parseInt

// Activity for Objective Match Collection to scout the objective gameplay of a single team in a match.
class CollectionObjectiveActivity : CollectionActivity() {
    private var isTimerRunning = false

    //FALSE = LOW
    private var removedTimelineActions: ArrayList<HashMap<String, String>> = ArrayList()
    companion object {
        var comingBack: String? = ""
    }

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
        when {
            !is_teleop_activated and (parseInt(match_time) < parseInt(getString(R.string.final_auto_time))) -> {
                timelineAdd(
                    match_time = getString(R.string.final_auto_time),
                    action_type = action_type
                )
            }
            is_teleop_activated and (parseInt(match_time) > parseInt(getString(R.string.initial_teleop_time))) -> {
                timelineAdd(
                    match_time = getString(R.string.initial_teleop_time),
                    action_type = action_type
                )
            }
            else -> {
                timelineAdd(match_time = match_time, action_type = action_type)
            }
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
            Constants.ActionType.SCORE_BALL_HIGH.toString() -> {
                numActionTwo--
                setCounterTexts()
            }
            Constants.ActionType.INTAKE.toString() -> {
                numActionFive--
                setCounterTexts()
            }
            Constants.ActionType.CLIMB_ATTEMPT.toString() -> {
                did_climb = false
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
            Constants.ActionType.SCORE_BALL_HIGH.toString() -> {
                numActionTwo++
                setCounterTexts()
            }
            Constants.ActionType.INTAKE.toString() -> {
                numActionFive++
                setCounterTexts()
            }
            Constants.ActionType.CLIMB_ATTEMPT.toString() -> {
                did_climb = true
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
    fun enableButtons() {
        val isIncap = tb_action_one.isChecked
        // Enable and disable buttons based on values of condition booleans defined previously.
        btn_action_one.isEnabled = (comingBack == "match information edit") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_two.isEnabled = (comingBack == "match information edit") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_five.isEnabled = (comingBack == "match information edit") or !(!isTimerRunning or popup_open or isIncap)

        tb_action_one.isEnabled = !(!is_teleop_activated or popup_open)

        btn_climb.isEnabled = !(!is_teleop_activated or popup_open or isIncap or did_climb)
        btn_climb.text =
            if (did_climb) getString(R.string.btn_climbed)
            else getString(R.string.btn_climb)
        btn_undo.isEnabled = (timeline.size > 0) and !popup_open
        btn_redo.isEnabled = (removedTimelineActions.size > 0) and !popup_open

        btn_timer.isEnabled = !((timeline.size > 0) or is_teleop_activated or popup_open)
        btn_proceed_edit.isEnabled =
            ((!is_teleop_activated) or (is_match_time_ended)) and !popup_open
    }

    // Function to end incap if still activated at end of the match.
    fun endAction() {
        if (tb_action_one.isChecked) {
            tb_action_one.isChecked = false
            timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
        }
    }

    // Set high and low goal counter values
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
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_LOW)
            numActionOne++
            setCounterTexts()
        }

        // Increment button action two by one when clicked and add action to timeline.
        btn_action_two.setOnClickListener {
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_HIGH)
            numActionTwo++
            setCounterTexts()
        }

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
            enableButtons()

            // OnClickListeners for the buttons in the climb popup
            popupView.btn_climb_cancel.setOnClickListener {
                did_climb = false
                climb_level = Constants.ClimbLevel.NONE
                popupWindow.dismiss()
                popup_open = false
                enableButtons()
            }
            popupView.btn_climb_done.setOnClickListener {
                popupWindow.dismiss()
                btn_climb.isEnabled = false
                popup_open = false
                timelineAdd(match_time, Constants.ActionType.CLIMB_ATTEMPT)
                enableButtons()
            }

            popupView.btn_climb_lv0.isActivated = false
            climb_level = Constants.ClimbLevel.NONE

            popupView.btn_climb_lv0.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = true
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.NONE
                did_climb = true
                popupView.btn_climb_done.isEnabled = did_climb
            }
            popupView.btn_climb_lv1.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = true
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.LOW
                did_climb = true
                popupView.btn_climb_done.isEnabled = did_climb
            }
            popupView.btn_climb_lv2.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = true
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.MID
                did_climb = true
                popupView.btn_climb_done.isEnabled = did_climb
            }
            popupView.btn_climb_lv3.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = true
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.HIGH
                did_climb = true
                popupView.btn_climb_done.isEnabled = did_climb
            }
            popupView.btn_climb_lv4.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = true
                climb_level = Constants.ClimbLevel.TRAVERSAL
                did_climb = true
                popupView.btn_climb_done.isEnabled = did_climb
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

    // resets and enables everything if you entered this screen by pressing the down button
    private fun comingBack() {
        if (comingBack == "match information edit") {
            isTimerRunning = false
            Log.d("coming-back", "came back")
            btn_proceed_edit.text = getString(R.string.btn_proceed)
            btn_proceed_edit.isEnabled = true
            btn_timer.isEnabled = false
            btn_timer.text = getString(R.string.timer_run_down)
            enableButtons()
        }
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

        comingBack()
        if (!(comingBack == "match information edit")) {
            timerReset()
        }
        setCounterTexts()
        initOnClicks()
        initTeamNum()

    }
}
