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
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.charge_popup.view.*
import kotlinx.android.synthetic.main.collection_objective_intake_activity.*
import kotlinx.android.synthetic.main.collection_objective_scoring_activity.*
import kotlinx.android.synthetic.main.collection_subjective_activity.*
import java.lang.Integer.parseInt

// Activity for Objective Match Collection to scout the objective gameplay of a single team in a match.
class CollectionObjectiveActivity : CollectionActivity() {
    /*private var btnActionList = listOf<Button?>(btn_action_one, btn_action_two, btn_action_three,
        btn_action_four, btn_action_five, btn_action_six, btn_action_seven, btn_action_eight,
        btn_action_nine, btn_action_ten)*/

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
        btn_timer_intake.text = getString(R.string.btn_timer_start)
        btn_timer_scoring.text = getString(R.string.btn_timer_start)
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
            Constants.ActionType.STATION_INTAKE.toString() -> {
                numActionOne--
                setCounterTexts()
            }
            Constants.ActionType.LOW_ROW_INTAKE.toString() -> {
                numActionTwo--
                setCounterTexts()
            }
            Constants.ActionType.GROUND_INTAKE.toString() -> {
                numActionThree--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CUBE_HIGH.toString() -> {
                numActionFour--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CUBE_MID.toString() -> {
                numActionFive--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CUBE_LOW.toString() -> {
                numActionSix--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CONE_HIGH.toString() -> {
                numActionSeven--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CONE_MID.toString() -> {
                numActionEight--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CONE_LOW.toString() -> {
                numActionNine--
                setCounterTexts()
            }
            Constants.ActionType.FAIL.toString() -> {
                numActionTen--
                setCounterTexts()
            }
            Constants.ActionType.CHARGE_ATTEMPT.toString() -> {
                did_charge = false
            }
            Constants.ActionType.START_INCAP.toString() -> {
                tb_action_one_intake.isChecked = false
                tb_action_one_scoring.isChecked = false
            }
            Constants.ActionType.END_INCAP.toString() -> {
                tb_action_one_intake.isChecked = true
                tb_action_one_scoring.isChecked = true
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
            Constants.ActionType.STATION_INTAKE.toString() -> {
                numActionOne++
                setCounterTexts()
            }
            Constants.ActionType.LOW_ROW_INTAKE.toString() -> {
                numActionTwo++
                setCounterTexts()
            }
            Constants.ActionType.GROUND_INTAKE.toString() -> {
                numActionThree++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CUBE_HIGH.toString() -> {
                numActionFour++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CUBE_MID.toString() -> {
                numActionFive++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CUBE_LOW.toString() -> {
                numActionSix++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CONE_HIGH.toString() -> {
                numActionSeven++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CONE_MID.toString() -> {
                numActionEight++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_CONE_LOW.toString() -> {
                numActionNine++
                setCounterTexts()
            }
            Constants.ActionType.FAIL.toString() -> {
                numActionTen++
                setCounterTexts()
            }
            Constants.ActionType.CHARGE_ATTEMPT.toString() -> {
                did_charge = true
            }
            Constants.ActionType.START_INCAP.toString() -> {
                tb_action_one_scoring.isChecked = true
                tb_action_one_intake.isChecked = true
            }
            Constants.ActionType.END_INCAP.toString() -> {
                tb_action_one_scoring.isChecked = false
                tb_action_one_intake.isChecked = false
            }
        }

        // Remove the redone action from removedTimelineActions.
        removedTimelineActions.removeAt(removedTimelineActions.size - 1)
        enableButtons()
        if (replaceOneMore) timelineReplace()
    }

    // Enable and disable buttons based on actions in timeline and timer stage.
    fun enableButtons() {
        val isIncap = tb_action_one_intake.isChecked || tb_action_one_scoring.isChecked
        // Enable and disable buttons based on values of condition booleans defined previously.
        /*for (btn in btnActionList) {
            btn?.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)
        }*/
        btn_action_one.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_two.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_three.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_four.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_five.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_six.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_seven.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_eight.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_nine.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)
        btn_action_ten.isEnabled = (comingBack == "match information edit") or (comingBack == "QRGenerate") or !(!isTimerRunning or popup_open or isIncap)

        tb_action_one_scoring.isEnabled = !(!is_teleop_activated or popup_open)
        tb_action_one_intake.isEnabled = !(!is_teleop_activated or popup_open)

        btn_charge_intake.isEnabled = !(!is_teleop_activated or popup_open or isIncap or did_charge)
        btn_charge_scoring.isEnabled = !(!is_teleop_activated or popup_open or isIncap or did_charge)

        btn_charge_intake.text =
            if (did_charge) getString(R.string.btn_charged)
            else getString(R.string.btn_charge)
        btn_undo_intake.isEnabled = (timeline.size > 0) and !popup_open
        btn_redo_intake.isEnabled = (removedTimelineActions.size > 0) and !popup_open

        btn_charge_scoring.text =
            if (did_charge) getString(R.string.btn_charged)
            else getString(R.string.btn_charge)
        btn_undo_scoring.isEnabled = (timeline.size > 0) and !popup_open
        btn_redo_scoring.isEnabled = (removedTimelineActions.size > 0) and !popup_open

        btn_timer_intake.isEnabled = !((timeline.size > 0) or is_teleop_activated or popup_open)
        btn_proceed_edit.isEnabled =
            ((!is_teleop_activated) or (is_match_time_ended)) and !popup_open

        btn_timer_scoring.isEnabled = !((timeline.size > 0) or is_teleop_activated or popup_open)
        btn_proceed_edit.isEnabled =
            ((!is_teleop_activated) or (is_match_time_ended)) and !popup_open
    }

    // Function to end incap if still activated at end of the match.
    fun endAction() {
        if (tb_action_one_intake.isChecked) {
            tb_action_one_intake.isChecked = false
            timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
        }
        if (tb_action_one_scoring.isChecked) {
            tb_action_one_scoring.isChecked = false
            timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
        }
    }

    // Set high and low goal counter values
    private fun setCounterTexts() {
        btn_action_one.text = getString(R.string.btn_action_one, numActionOne.toString())
        btn_action_two.text = getString(R.string.btn_action_two, numActionTwo.toString())
        btn_action_three.text = getString(R.string.btn_action_three, numActionThree.toString())
        btn_action_four.text = getString(R.string.btn_action_four, numActionFour.toString())
        btn_action_five.text = getString(R.string.btn_action_five, numActionFive.toString())
        btn_action_six.text = getString(R.string.btn_action_six, numActionSix.toString())
        btn_action_seven.text = getString(R.string.btn_action_seven, numActionSeven.toString())
        btn_action_eight.text = getString(R.string.btn_action_eight, numActionEight.toString())
        btn_action_nine.text = getString(R.string.btn_action_nine, numActionNine.toString())
        btn_action_ten.text = getString(R.string.btn_action_ten, numActionTen.toString())
    }

    // Initialize button and toggle button onClickListeners.
    private fun initOnClicks() {
        btn_proceed_edit.setOnClickListener {
            if (!is_teleop_activated) {
                is_teleop_activated = true
                enableButtons()
                btn_proceed_edit.text = getString(R.string.btn_proceed)
                btn_proceed_edit.isEnabled = false
                btn_timer_intake.isEnabled = false
                btn_timer_scoring.isEnabled = false
                objective_match_collection_layout_intake.setBackgroundColor(Color.WHITE)
                objective_match_collection_layout_scoring.setBackgroundColor(Color.WHITE)

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
        btn_timer_intake.setOnClickListener {
            if (!isTimerRunning) {
                TimerUtility.MatchTimerThread()
                    .initTimer(
                        context = this,
                        btn_timer = btn_timer_intake,
                        btn_proceed = btn_proceed_edit,
                        btn_incap = tb_action_one_intake,
                        layout = objective_match_collection_layout_intake
                    )
                isTimerRunning = true
                enableButtons()
                btn_proceed_edit.isEnabled = true
            }
        }
        btn_timer_scoring.setOnClickListener {
            if (!isTimerRunning) {
                TimerUtility.MatchTimerThread()
                    .initTimer(
                        context = this,
                        btn_timer = btn_timer_scoring,
                        btn_proceed = btn_proceed_edit,
                        btn_incap = tb_action_one_scoring,
                        layout = objective_match_collection_layout_scoring
                    )
                isTimerRunning = true
                enableButtons()
                btn_proceed_edit.isEnabled = true
            }
        }

        // Reset timer on long click if timer is running.
        btn_timer_intake.setOnLongClickListener(View.OnLongClickListener {
            if (isTimerRunning and !is_teleop_activated) {
                timerReset()
                timeline = ArrayList()
                isTimerRunning = false
                is_teleop_activated = false
                enableButtons()
                btn_proceed_edit.isEnabled = false
                btn_proceed_edit.text = getString(R.string.btn_to_teleop)
                objective_match_collection_layout_intake.setBackgroundColor(Color.WHITE)
            }
            return@OnLongClickListener true
        })

        btn_timer_scoring.setOnLongClickListener(View.OnLongClickListener {
            if (isTimerRunning and !is_teleop_activated) {
                timerReset()
                timeline = ArrayList()
                isTimerRunning = false
                is_teleop_activated = false
                enableButtons()
                btn_proceed_edit.isEnabled = false
                btn_proceed_edit.text = getString(R.string.btn_to_teleop)
                objective_match_collection_layout_scoring.setBackgroundColor(Color.WHITE)
            }
            return@OnLongClickListener true
        })

        // Increment button action one by one when clicked and add action to timeline.
        btn_action_one.setOnClickListener {
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.STATION_INTAKE)
            numActionOne++
            setCounterTexts()
        }

        // Increment button action two by one when clicked and add action to timeline.
        btn_action_two.setOnClickListener {
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.LOW_ROW_INTAKE)
            numActionTwo++
            setCounterTexts()
        }

        // Increment button action five by one when clicked and add action to timeline.
        btn_action_three.setOnClickListener {
            timelineAddWithStage(action_type = Constants.ActionType.GROUND_INTAKE)
            numActionThree++
            setCounterTexts()
        }

        btn_action_four.setOnClickListener {
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_CUBE_HIGH)
            numActionFour++
            setCounterTexts()
        }

        btn_action_five.setOnClickListener {
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_CUBE_MID)
            numActionFive++
            setCounterTexts()
        }

        btn_action_six.setOnClickListener {
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_CUBE_LOW)
            numActionSix++
            setCounterTexts()
        }

        btn_action_seven.setOnClickListener {
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_CONE_HIGH)
            numActionSeven++
            setCounterTexts()
        }

        btn_action_eight.setOnClickListener {
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_CONE_MID)
            numActionEight++
            setCounterTexts()
        }

        btn_action_nine.setOnClickListener {
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_CONE_LOW)
            numActionNine++
            setCounterTexts()
        }

        btn_action_ten.setOnClickListener {
            // FALSE = LOW
            timelineAddWithStage(action_type = Constants.ActionType.FAIL)
            numActionTen++
            setCounterTexts()
        }
        // Start incap if clicking the incap toggle button checks the toggle button.
        // Otherwise, end incap.
        tb_action_one_intake.setOnClickListener {
            if (tb_action_one_intake.isChecked) {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.START_INCAP)
            } else {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
            }
        }

        tb_action_one_scoring.setOnClickListener {
            if (tb_action_one_scoring.isChecked) {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.START_INCAP)
            } else {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
            }
        }

        // Open the Charge popup window.
        btn_charge_intake.setOnClickListener {
            val popupView = View.inflate(this, R.layout.charge_popup, null)
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val popupWindow = PopupWindow(popupView, width, height, false)
            popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)
            popup_open = true
            enableButtons()
        }

            btn_charge_scoring.setOnClickListener {
                val popupView = View.inflate(this, R.layout.charge_popup, null)
                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val popupWindow = PopupWindow(popupView, width, height, false)
                popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)
                popup_open = true
                enableButtons()

                // OnClickListeners for the buttons in the climb popup
                popupView.btn_charge_cancel.setOnClickListener {
                    did_charge = false
                    charge_level = Constants.ChargeLevel.NONE
                    popupWindow.dismiss()
                    popup_open = false
                    enableButtons()
                }

                popupView.btn_climb_done.setOnClickListener {
                    popupWindow.dismiss()
                    btn_charge_intake.isEnabled = false
                    btn_charge_scoring.isEnabled = false
                    popup_open = false
                    timelineAdd(match_time, Constants.ActionType.CHARGE_ATTEMPT)
                    enableButtons()
                }

                popupView.btn_failed.isActivated = false
                charge_level = Constants.ChargeLevel.NONE

                popupView.btn_failed.setOnClickListener {
                    popupView.btn_failed.isActivated = true
                    popupView.btn_parked.isActivated = false
                    popupView.btn_docked.isActivated = false
                    popupView.btn_engaged.isActivated = false
                    charge_level = Constants.ChargeLevel.FAILED
                    did_charge = true
                    popupView.btn_climb_done.isEnabled = did_charge
                }
                popupView.btn_parked.setOnClickListener {
                    popupView.btn_failed.isActivated = false
                    popupView.btn_parked.isActivated = true
                    popupView.btn_docked.isActivated = false
                    popupView.btn_engaged.isActivated = false
                    charge_level = Constants.ChargeLevel.PARKED
                    did_charge = true
                    popupView.btn_climb_done.isEnabled = did_charge
                }
                popupView.btn_docked.setOnClickListener {
                    popupView.btn_failed.isActivated = false
                    popupView.btn_parked.isActivated = false
                    popupView.btn_docked.isActivated = true
                    popupView.btn_engaged.isActivated = false
                    charge_level = Constants.ChargeLevel.DOCKED
                    did_charge = true
                    popupView.btn_climb_done.isEnabled = did_charge
                }
                popupView.btn_engaged.setOnClickListener {
                    popupView.btn_failed.isActivated = false
                    popupView.btn_parked.isActivated = false
                    popupView.btn_docked.isActivated = false
                    popupView.btn_engaged.isActivated = true
                    charge_level = Constants.ChargeLevel.ENGAGED
                    did_charge = true
                    popupView.btn_climb_done.isEnabled = did_charge
                }
            }

            // Remove previous action from timeline when undo button is clicked.
            btn_undo_intake.setOnClickListener {
                timelineRemove()
            }

            btn_undo_scoring.setOnClickListener {
                timelineRemove()
            }

        // Replace previously undone action to timeline when redo button is clicked.
        btn_redo_intake.setOnClickListener {
            timelineReplace()
        }

        btn_redo_scoring.setOnClickListener {
                    timelineReplace()
        }
    }

    // Set team number view to team number defined in References.kt and set team number to alliance color.
    private fun initTeamNum() {
        tv_team_number_intake.text = team_number
        tv_team_number_scoring.text = team_number

        if (alliance_color == Constants.AllianceColor.RED) {
            tv_team_number_intake.setTextColor(resources.getColor(R.color.alliance_red_light, null))
            tv_team_number_scoring.setTextColor(resources.getColor(R.color.alliance_red_light, null))

        } else if (alliance_color == Constants.AllianceColor.BLUE) {
            tv_team_number_intake.setTextColor(resources.getColor(R.color.alliance_blue_light, null))
            tv_team_number_scoring.setTextColor(resources.getColor(R.color.alliance_blue_light, null))

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

    // resets and enables everything if you entered this screen by pressing the back button
    private fun comingBack() {
        if ((comingBack == "match information edit") or (comingBack == "QRGenerate")) {
            isTimerRunning = false
            Log.d("coming-back", "came back")
            btn_proceed_edit.text = getString(R.string.btn_proceed)
            btn_proceed_edit.isEnabled = true
            btn_timer_intake.isEnabled = false
            btn_timer_scoring.isEnabled = false
            btn_timer_intake.text = getString(R.string.timer_run_down)
            btn_timer_scoring.text = getString(R.string.timer_run_down)
            enableButtons()
        }
    }

    // Restart app from StartingPositionObjectiveActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> intentToPreviousActivity() }
                .show()
            comingBack = "collection objective activity"
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("bobbo", "gets into onCreate")
        setContentView(R.layout.collection_objective_intake_activity)

        comingBack()
        if ((comingBack != "match information edit") and (comingBack != "QRGenerate")) {
            timerReset()
        }
        setCounterTexts()
        initOnClicks()
        initTeamNum()

    }
}
