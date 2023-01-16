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
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.charge_popup.view.btn_charge_cancel
import kotlinx.android.synthetic.main.charge_popup.view.btn_climb_done
import kotlinx.android.synthetic.main.charge_popup.view.btn_docked
import kotlinx.android.synthetic.main.charge_popup.view.btn_engaged
import kotlinx.android.synthetic.main.charge_popup.view.btn_failed
import kotlinx.android.synthetic.main.charge_popup.view.btn_parked
import kotlinx.android.synthetic.main.collection_objective_activity.btn_charge
import kotlinx.android.synthetic.main.collection_objective_activity.btn_proceed_edit
import kotlinx.android.synthetic.main.collection_objective_activity.btn_redo
import kotlinx.android.synthetic.main.collection_objective_activity.btn_timer
import kotlinx.android.synthetic.main.collection_objective_activity.btn_undo
import kotlinx.android.synthetic.main.collection_objective_activity.objective_match_collection_layout
import kotlinx.android.synthetic.main.collection_objective_activity.tb_action_one
import kotlinx.android.synthetic.main.collection_objective_activity.tv_team_number

/**
 * Activity for Objective Match Collection to scout the objective gameplay of a single team in a
 * match.
 */
class CollectionObjectiveActivity : CollectionActivity() {

    var scoringScreen = true
        set(value) {
            field = value
            supportFragmentManager.beginTransaction()
                .replace(R.id.action_btn_frame, if (value) scoringPanel else intakePanel).commit()
            enableButtons()
        }

    private val scoringPanel = ObjectiveScoringFragment()
    private val intakePanel = ObjectiveIntakeFragment()

    var isTimerRunning = false

    val isIncap get() = tb_action_one.isChecked

    private var removedTimelineActions = mutableListOf<Map<String, String>>()

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

    /**
     * Add performed action to timeline, including action type and time of action.
     */
    private fun timelineAdd(matchTime: String, actionType: Constants.ActionType) {
        timeline.add(mapOf("match_time" to matchTime, "action_type" to "$actionType"))
        removedTimelineActions.clear()

        enableButtons()
    }

    /**
     * If stage and time contradict when action is recorded, add action to timeline with time value
     * dictated by stage.
     */
    fun timelineAddWithStage(action_type: Constants.ActionType) = when {
        !is_teleop_activated and (match_time.toInt() < getString(R.string.final_auto_time).toInt()) -> {
            timelineAdd(
                matchTime = getString(R.string.final_auto_time),
                actionType = action_type
            )
        }

        is_teleop_activated and (match_time.toInt() > getString(R.string.initial_teleop_time).toInt()) -> {
            timelineAdd(
                matchTime = getString(R.string.initial_teleop_time),
                actionType = action_type
            )
        }

        else -> timelineAdd(matchTime = match_time, actionType = action_type)
    }

    /**
     * Remove previously inputted action from timeline.
     */
    private fun timelineRemove() {
        // Decrement action values displayed on action counters.
        when (timeline[timeline.size - 1]["action_type"].toString()) {
            Constants.ActionType.INTAKE_STATION.toString() -> {
                numActionOne--
                scoringScreen = false
            }

            Constants.ActionType.INTAKE_LOW_ROW.toString() -> {
                numActionTwo--
                scoringScreen = false
            }

            Constants.ActionType.INTAKE_GROUND.toString() -> {
                numActionThree--
                scoringScreen = false
            }

            Constants.ActionType.SCORE_CUBE_HIGH.toString() -> {
                numActionFour--
                scoringScreen = true
            }

            Constants.ActionType.SCORE_CUBE_MID.toString() -> {
                numActionFive--
                scoringScreen = true
            }

            Constants.ActionType.SCORE_CUBE_LOW.toString() -> {
                numActionSix--
                scoringScreen = true
            }

            Constants.ActionType.SCORE_CONE_HIGH.toString() -> {
                numActionSeven--
                scoringScreen = true
            }

            Constants.ActionType.SCORE_CONE_MID.toString() -> {
                numActionEight--
                scoringScreen = true
            }

            Constants.ActionType.SCORE_CONE_LOW.toString() -> {
                numActionNine--
                scoringScreen = true
            }

            Constants.ActionType.FAIL.toString() -> {
                numActionTen--
                scoringScreen = true
            }

            Constants.ActionType.CHARGE_ATTEMPT.toString() -> {
                if (is_teleop_activated) did_tele_charge = false
                else did_auto_charge = false
            }

            Constants.ActionType.START_INCAP.toString() -> tb_action_one.isChecked = false
            Constants.ActionType.END_INCAP.toString() -> tb_action_one.isChecked = true
            Constants.ActionType.TO_TELEOP.toString() -> is_teleop_activated = false
        }

        // Add removed action to removedTimelineActions so it can be redone if needed.
        removedTimelineActions.add(timeline.last())

        // Remove most recent timeline entry.
        timeline.removeAt(timeline.lastIndex)
        enableButtons()
    }

    /**
     * Redoes timeline actions after undo.
     */
    private fun timelineReplace() {

        // Add most recently undone action from removedTimelineActions back to timeline.
        timeline.add(removedTimelineActions.last())

        // Increment action values and display on action counters if re-adding a counter action from the timeline.
        when (removedTimelineActions[removedTimelineActions.size - 1]["action_type"].toString()) {
            Constants.ActionType.INTAKE_STATION.toString() -> {
                numActionOne++
                scoringScreen = true
            }

            Constants.ActionType.INTAKE_LOW_ROW.toString() -> {
                numActionTwo++
                scoringScreen = true
            }

            Constants.ActionType.INTAKE_GROUND.toString() -> {
                numActionThree++
                scoringScreen = true
            }

            Constants.ActionType.SCORE_CUBE_HIGH.toString() -> {
                numActionFour++
                scoringScreen = false
            }

            Constants.ActionType.SCORE_CUBE_MID.toString() -> {
                numActionFive++
                scoringScreen = false
            }

            Constants.ActionType.SCORE_CUBE_LOW.toString() -> {
                numActionSix++
                scoringScreen = false
            }

            Constants.ActionType.SCORE_CONE_HIGH.toString() -> {
                numActionSeven++
                scoringScreen = false
            }

            Constants.ActionType.SCORE_CONE_MID.toString() -> {
                numActionEight++
                scoringScreen = false
            }

            Constants.ActionType.SCORE_CONE_LOW.toString() -> {
                numActionNine++
                scoringScreen = false
            }

            Constants.ActionType.FAIL.toString() -> {
                numActionTen++
                scoringScreen = false
            }

            Constants.ActionType.CHARGE_ATTEMPT.toString() -> {
                if (is_teleop_activated) did_tele_charge = true
                else did_auto_charge = true
            }

            Constants.ActionType.START_INCAP.toString() -> tb_action_one.isChecked = true
            Constants.ActionType.END_INCAP.toString() -> tb_action_one.isChecked = false
            Constants.ActionType.TO_TELEOP.toString() -> is_teleop_activated = true
        }

        // Remove the redone action from removedTimelineActions.
        removedTimelineActions.removeAt(removedTimelineActions.lastIndex)
        enableButtons()
    }

    /**
     * Enable and disable buttons based on actions in timeline and timer stage.
     */
    fun enableButtons() {
        if (!scoringScreen) {
            intakePanel.enableButtons(isIncap)
        } else {
            scoringPanel.enableButtons(isIncap)
        }
        tb_action_one.isEnabled = !(!is_teleop_activated or popup_open)

        btn_charge.isEnabled = isTimerRunning && !(popup_open || isIncap ||
                ((is_teleop_activated && did_tele_charge) || (!is_teleop_activated && did_auto_charge)))

        btn_charge.text =
            if (isTimerRunning && ((is_teleop_activated && did_tele_charge) || (!is_teleop_activated && did_auto_charge))) getString(
                R.string.btn_charged
            )
            else getString(R.string.btn_charge)
        btn_undo.isEnabled = (timeline.size > 0) and !popup_open
        btn_redo.isEnabled = (removedTimelineActions.size > 0) and !popup_open

        btn_timer.isEnabled = !((timeline.size > 0) or is_teleop_activated or popup_open)
        btn_proceed_edit.isEnabled =
            ((!is_teleop_activated) or (is_match_time_ended)) and !popup_open
        btn_proceed_edit.text = if (!is_teleop_activated) getString(R.string.btn_to_teleop)
        else getString(R.string.btn_proceed)
    }

    /**
     * Ends incap if still activated at end of the match.
     */
    fun endAction() {
        if (tb_action_one.isChecked) {
            tb_action_one.isChecked = false
            timelineAdd(matchTime = match_time, actionType = Constants.ActionType.END_INCAP)
        }
    }

    /**
     * Initialize button and toggle button `onClickListeners`.
     */
    private fun initOnClicks() {
        btn_proceed_edit.setOnClickListener {
            if (!is_teleop_activated) {
                is_teleop_activated = true
                timelineAdd(match_time, Constants.ActionType.TO_TELEOP)
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

        // Start incap if clicking the incap toggle button checks the toggle button.
        // Otherwise, end incap.
        tb_action_one.setOnClickListener {
            if (tb_action_one.isChecked) {
                timelineAdd(matchTime = match_time, actionType = Constants.ActionType.START_INCAP)
            } else {
                timelineAdd(matchTime = match_time, actionType = Constants.ActionType.END_INCAP)
            }
        }

        // Open the Charge popup window.
        btn_charge.setOnClickListener {
            val popupView = View.inflate(this, R.layout.charge_popup, null)
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val popupWindow = PopupWindow(popupView, width, height, false)
            popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)
            popup_open = true
            if (!is_teleop_activated) popupView.btn_parked.isVisible = false
            timelineAdd(match_time, Constants.ActionType.CHARGE_ATTEMPT)
            enableButtons()

            // OnClickListeners for the buttons in the climb popup
            popupView.btn_charge_cancel.setOnClickListener {
                if (is_teleop_activated) {
                    tele_charge_level = Constants.ChargeLevel.NONE
                    did_tele_charge = false
                } else {
                    auto_charge_level = Constants.ChargeLevel.NONE
                    did_auto_charge = false
                }
                popupWindow.dismiss()
                popup_open = false
                timeline.removeAt(timeline.lastIndex)
                enableButtons()
            }

            popupView.btn_climb_done.setOnClickListener {
                popupWindow.dismiss()
                btn_charge.isEnabled = false
                popup_open = false
                enableButtons()
            }

            popupView.btn_failed.isActivated = false
            if (is_teleop_activated) tele_charge_level = Constants.ChargeLevel.NONE
            else auto_charge_level = Constants.ChargeLevel.NONE

            popupView.btn_failed.setOnClickListener {
                popupView.btn_failed.isActivated = true
                popupView.btn_parked.isActivated = false
                popupView.btn_docked.isActivated = false
                popupView.btn_engaged.isActivated = false
                if (is_teleop_activated) {
                    tele_charge_level = Constants.ChargeLevel.FAILED
                    did_tele_charge = true
                    popupView.btn_climb_done.isEnabled = did_tele_charge
                } else {
                    auto_charge_level = Constants.ChargeLevel.FAILED
                    did_auto_charge = true
                    popupView.btn_climb_done.isEnabled = did_auto_charge
                }
            }
            popupView.btn_parked.setOnClickListener {
                popupView.btn_failed.isActivated = false
                popupView.btn_parked.isActivated = true
                popupView.btn_docked.isActivated = false
                popupView.btn_engaged.isActivated = false
                if (is_teleop_activated) {
                    tele_charge_level = Constants.ChargeLevel.PARK
                    did_tele_charge = true
                    popupView.btn_climb_done.isEnabled = did_tele_charge
                } else {
                    auto_charge_level = Constants.ChargeLevel.PARK
                    did_auto_charge = true
                    popupView.btn_climb_done.isEnabled = did_auto_charge
                }
            }
            popupView.btn_docked.setOnClickListener {
                popupView.btn_failed.isActivated = false
                popupView.btn_parked.isActivated = false
                popupView.btn_docked.isActivated = true
                popupView.btn_engaged.isActivated = false
                if (is_teleop_activated) {
                    tele_charge_level = Constants.ChargeLevel.DOCKED
                    did_tele_charge = true
                    popupView.btn_climb_done.isEnabled = did_tele_charge
                } else {
                    auto_charge_level = Constants.ChargeLevel.DOCKED
                    did_auto_charge = true
                    popupView.btn_climb_done.isEnabled = did_auto_charge
                }
            }
            popupView.btn_engaged.setOnClickListener {
                popupView.btn_failed.isActivated = false
                popupView.btn_parked.isActivated = false
                popupView.btn_docked.isActivated = false
                popupView.btn_engaged.isActivated = true
                if (is_teleop_activated) {
                    tele_charge_level = Constants.ChargeLevel.ENGAGED
                    did_tele_charge = true
                    popupView.btn_climb_done.isEnabled = did_tele_charge
                } else {
                    auto_charge_level = Constants.ChargeLevel.ENGAGED
                    did_auto_charge = true
                    popupView.btn_climb_done.isEnabled = did_auto_charge
                }
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

    /**
     * Set team number view to team number defined in `References` and set team number to alliance
     * color.
     */
    private fun initTeamNum() {
        tv_team_number.text = team_number

        if (alliance_color == Constants.AllianceColor.RED) {
            tv_team_number.setTextColor(resources.getColor(R.color.alliance_red_light, null))

        } else if (alliance_color == Constants.AllianceColor.BLUE) {
            tv_team_number.setTextColor(resources.getColor(R.color.alliance_blue_light, null))

        }
    }

    /**
     * Initialize intent used in [onKeyLongPress] to restart app from
     * [StartingPositionObjectiveActivity].
     */
    private fun intentToPreviousActivity() {
        is_teleop_activated = false
        startActivity(
            Intent(this, StartingPositionObjectiveActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    /**
     * Resets and enables everything if the user entered this screen by pressing the back button.
     */
    private fun comingBack() {
        if ((comingBack == "match information edit") or (comingBack == "QRGenerate")) {
            isTimerRunning = false
            Log.d("coming-back", "came back")
            btn_proceed_edit.text = getString(R.string.btn_proceed)
            btn_proceed_edit.isEnabled = true
            btn_timer.isEnabled = false
            btn_timer.text = getString(R.string.timer_run_down)
            enableButtons()
        }
    }

    /**
     * Restart app from [StartingPositionObjectiveActivity] when back button is long pressed.
     */
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
        setContentView(R.layout.collection_objective_activity)

        // Set the currently displayed fragment to the scoring panel
        supportFragmentManager.beginTransaction().add(
            R.id.action_btn_frame,
            if (preloaded == Constants.Preloaded.NONE) intakePanel else scoringPanel
        ).commit()

        comingBack()
        if ((comingBack != "match information edit") and (comingBack != "QRGenerate")) {
            timerReset()
        }

        initOnClicks()
        initTeamNum()
    }
}
