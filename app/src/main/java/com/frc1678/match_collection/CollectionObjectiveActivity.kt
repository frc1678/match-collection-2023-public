// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
    private var numActionOne = 0 //SCORE_BALL_HIGH_NEAR_HUB
    private var numActionTwo = 0 //SCORE_BALL_HIGH_FAR_HUB
    private var numActionThree = 0 //SCORE_BALL_HIGH_NEAR_OTHER
    private var numActionFour = 0 //SCORE_BALL_HIGH_FAR_OTHER
    private var numActionFive = 0 //SCORE_BALL_LAUNCHPAD
    private var numActionSeven = 0  //SCORE_BALL_LOW_NEAR_HUB
    private var numActionEight = 0 //SCORE_BALL_LOW_FAR_HUB
    private var numActionNine = 0 //CATCH_CARGO
    private var numActionTen = 0 //SCORE_OPPOSING_BALL
    private var isTimerRunning = false
    //FALSE = LOW
    private var goalTypeIsHigh = false
    private var numActionSix = 0 //NUMBER OF INTAKES
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
            Constants.ActionType.HIGH_LOW_TOGGLE.toString() -> {
                goalTypeIsHigh = !goalTypeIsHigh
                setCounterTexts()
                setBackgrounds()
                enableButtons()
            }
            Constants.ActionType.SCORE_BALL_HIGH_NEAR_HUB.toString() -> {
                numActionOne--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_HIGH_LAUNCHPAD.toString() -> {
                numActionFive--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_HIGH_NEAR_OTHER.toString() -> {
                numActionThree--
                setCounterTexts()
            }
            Constants.ActionType.INTAKE.toString() -> {
                numActionSix--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_HIGH_FAR_HUB.toString() -> {
                numActionTwo--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_HIGH_FAR_OTHER.toString() -> {
                numActionFour--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_LOW_NEAR_HUB.toString() -> {
                numActionSeven--
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_LOW_FAR_HUB.toString() -> {
                numActionEight--
                setCounterTexts()
            }
            Constants.ActionType.CATCH_EXIT_BALL.toString() -> {
                numActionNine--
            }
            Constants.ActionType.SCORE_OPPONENT_BALL.toString() -> {
                numActionTen--
            }
            Constants.ActionType.END_CLIMB.toString() -> {
                removeOneMore = true
                climb_timer_paused = false
                climb_timer = null
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
            Constants.ActionType.HIGH_LOW_TOGGLE.toString() -> {
                goalTypeIsHigh = !goalTypeIsHigh
                setCounterTexts()
                setBackgrounds()
                enableButtons()
            }
            Constants.ActionType.SCORE_BALL_HIGH_NEAR_HUB.toString() -> {
                numActionOne++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_HIGH_LAUNCHPAD.toString() -> {
                numActionFive++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_HIGH_NEAR_OTHER.toString() -> {
                numActionThree++
                setCounterTexts()
            }
            Constants.ActionType.INTAKE.toString() -> {
                numActionSix++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_HIGH_FAR_HUB.toString() -> {
                numActionTwo++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_HIGH_FAR_OTHER.toString() -> {
                numActionFour++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_LOW_NEAR_HUB.toString() -> {
                numActionSeven++
                setCounterTexts()
            }
            Constants.ActionType.SCORE_BALL_LOW_FAR_HUB.toString() -> {
                numActionEight++
                setCounterTexts()
            }
            Constants.ActionType.CATCH_EXIT_BALL.toString() -> {
                numActionNine++
            }
            Constants.ActionType.SCORE_OPPONENT_BALL.toString() -> {
                numActionTen++
            }
            Constants.ActionType.START_CLIMB.toString() -> {
                replaceOneMore = true
                climb_timer_paused = true
                climb_timer = null
            }
        }

        // Remove the redone action from removedTimelineActions.
        removedTimelineActions.removeAt(removedTimelineActions.size - 1)

        enableButtons()

        if (replaceOneMore) timelineReplace()
    }

    // Enable and disable buttons based on actions in timeline and timer stage.
    private fun enableButtons() {
        var isIncap = false

        // Define condition booleans based on actions in timeline, if existent.
        if (timeline.size > 0) {
            isIncap =
                timeline[timeline.size - 1].containsValue(Constants.ActionType.START_INCAP.toString())
        }

        // Enable and disable buttons based on values of condition booleans defined previously.
        btn_action_one.isEnabled = !(!isTimerRunning or popup_open or isIncap)
        btn_action_two.isEnabled = !(!isTimerRunning or popup_open or isIncap)
        btn_action_three.isEnabled = !(!isTimerRunning or popup_open or isIncap or !goalTypeIsHigh)
        btn_action_four.isEnabled = !(!isTimerRunning or popup_open or isIncap or !goalTypeIsHigh)
        btn_action_five.isEnabled = !(!isTimerRunning or popup_open or isIncap or !goalTypeIsHigh)

        btn_action_six.isEnabled = !(!isTimerRunning or popup_open or isIncap)

        btn_error.isEnabled = !(!isTimerRunning or popup_open or isIncap)

        tb_action_two.isEnabled = !(popup_open or isIncap or !isTimerRunning)
        tb_action_two.isChecked = (goalTypeIsHigh)

        tb_action_three.isEnabled = !(!is_teleop_activated or popup_open)
        tb_action_three.isChecked = (isIncap)

        btn_action_eleven.isEnabled = !(!is_teleop_activated or popup_open or isIncap or climb_timer_paused)
        btn_action_eleven.text =
            if (climb_timer_paused) getString(R.string.btn_climbed)
            else getString(R.string.btn_climb)

        btn_undo.isEnabled = (timeline.size > 0) and !popup_open
        btn_redo.isEnabled = (removedTimelineActions.size > 0) and !popup_open

        btn_timer.isEnabled = !((timeline.size > 0) or is_teleop_activated or popup_open)
        btn_proceed_edit.isEnabled = ((!is_teleop_activated) or (is_match_time_ended)) and !popup_open
    }

    // Function to end incap if still activated at end of the match.
    private fun endAction() {
        if (tb_action_three.isChecked) {
            tb_action_three.isChecked = false
            timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
        }
    }

    // Set high and low goal counter values.
    private fun setCounterTexts() {
        if (!goalTypeIsHigh) {
            btn_action_one.text = getString(R.string.btn_action_one, numActionSeven.toString())
            btn_action_two.text = getString(R.string.btn_action_one, numActionEight.toString())
        } else {
            btn_action_one.text = getString(R.string.btn_action_one, numActionOne.toString())
            btn_action_two.text = getString(R.string.btn_action_one, numActionTwo.toString())
        }
        btn_action_three.text = getString(R.string.btn_action_three, numActionThree.toString())
        btn_action_four.text = getString(R.string.btn_action_four, numActionFour.toString())
        btn_action_five.text = getString(R.string.btn_action_five, numActionFive.toString())
        btn_action_six.text = getString(R.string.btn_action_six, numActionSix.toString())
    }

    private fun setBackgrounds() {
        if (!goalTypeIsHigh) { //if Low
            btn_action_one.setBackgroundResource(R.drawable.btn_action_selector_low_near)
            btn_action_three.setBackgroundResource(R.drawable.btn_action_selector_low_near)
            btn_action_two.setBackgroundResource(R.drawable.btn_action_selector_low_far)
            btn_action_four.setBackgroundResource(R.drawable.btn_action_selector_low_far)
            tv_near.setTextColor(resources.getColor(R.color.low_near))
            tv_far.setTextColor(resources.getColor(R.color.low_far))
        } else {
            btn_action_one.setBackgroundResource(R.drawable.btn_action_selector_high_near)
            btn_action_three.setBackgroundResource(R.drawable.btn_action_selector_high_near)
            btn_action_five.setBackgroundResource(R.drawable.btn_action_selector_high_near)
            btn_action_two.setBackgroundResource(R.drawable.btn_action_selector_high_far)
            btn_action_four.setBackgroundResource(R.drawable.btn_action_selector_high_far)
            tv_near.setTextColor(resources.getColor(R.color.high_near))
            tv_far.setTextColor(resources.getColor(R.color.high_far_pressed))
        }
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
            if (!goalTypeIsHigh){
                timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_LOW_NEAR_HUB)
                numActionSeven++
            } else {
                timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_HIGH_NEAR_HUB)
                numActionOne++

            }
            setCounterTexts()
        }

        // Increment button action two by one when clicked and add action to timeline.
        btn_action_two.setOnClickListener {
            //FALSE = LOW
            if (!goalTypeIsHigh){
                timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_LOW_FAR_HUB)
                numActionEight++
            } else {
                timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_HIGH_FAR_HUB)
                numActionTwo++

            }
            setCounterTexts()
        }

        // Increment button action three by one when clicked and add action to timeline.
        btn_action_three.setOnClickListener {
            numActionThree++
            setCounterTexts()
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_HIGH_NEAR_OTHER)
        }

        // Increment button action four by one when clicked and add action to timeline.
        btn_action_four.setOnClickListener {
            numActionFour++
            setCounterTexts()
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_HIGH_FAR_OTHER)
        }

        // Increment button action five by one when clicked and add action to timeline.
        btn_action_five.setOnClickListener {
            numActionFive++
            setCounterTexts()
            timelineAddWithStage(action_type = Constants.ActionType.SCORE_BALL_HIGH_LAUNCHPAD)
        }

        // Increment button action six by one when clicked and add action to timeline.
        btn_action_six.setOnClickListener {
            numActionSix++
            setCounterTexts()
            timelineAddWithStage(action_type = Constants.ActionType.INTAKE)
        }

        // Start incap if clicking the incap toggle button checks the toggle button.
        // Otherwise, end incap.
        tb_action_three.setOnClickListener {
            if (tb_action_three.isChecked) {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.START_INCAP)
            } else {
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.END_INCAP)
            }
        }

        // Open the Climb popup window.
        btn_action_eleven.setOnClickListener {
            val popupView = View.inflate(this, R.layout.climb_popup, null)
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val popupWindow = PopupWindow(popupView, width, height, false)
            popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)
            popup_open = true
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
                climb_level = Constants.ClimbLevel.ZERO
                climb_start_time = null
                climb_end_time = null
                popupWindow.dismiss()
                popup_open = false
                enableButtons()
            }
            popupView.btn_climb_done.setOnClickListener {
                popupWindow.dismiss()
                btn_action_eleven.isEnabled = false
                popup_open = false
                climb_start_time?.let { it1 -> timelineAdd(it1, Constants.ActionType.START_CLIMB) }
                climb_end_time?.let { it1 -> timelineAdd(it1, Constants.ActionType.END_CLIMB) }
                enableButtons()
            }
            popupView.btn_climb_lv0.isActivated = true
            climb_level = Constants.ClimbLevel.ZERO
            popupView.btn_climb_lv0.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = true
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.ZERO
            }
            popupView.btn_climb_lv1.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = true
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.ONE
            }
            popupView.btn_climb_lv2.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = true
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.TWO
            }
            popupView.btn_climb_lv3.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = true
                popupView.btn_climb_lv4.isActivated = false
                climb_level = Constants.ClimbLevel.THREE
            }
            popupView.btn_climb_lv4.setOnClickListener {
                popupView.btn_climb_lv0.isActivated = false
                popupView.btn_climb_lv1.isActivated = false
                popupView.btn_climb_lv2.isActivated = false
                popupView.btn_climb_lv3.isActivated = false
                popupView.btn_climb_lv4.isActivated = true
                climb_level = Constants.ClimbLevel.FOUR
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
            popup_open = true
            enableButtons()
            val popupView = View.inflate(this, R.layout.error_pop_up,null)
            var errorReport : Int? = null
            popupView.catch_cargo.text = getString(R.string.btn_action_nine, numActionNine.toString())
            popupView.score_opp.text = getString(R.string.btn_action_ten, numActionTen.toString())
            // Inflate a custom view using layout inflater

            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(
                popupView, // Custom view to show in popup window
                LinearLayout.LayoutParams.MATCH_PARENT, // Width of popup window
                600, // Window height
                false
            )
            popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)

            popupView.catch_cargo.setOnClickListener{
                if(errorReport != 0) {
                    popupView.catch_cargo.isActivated = true
                    if (errorReport == 1) {
                        popupView.score_opp.isActivated = false
                        numActionTen--
                        popupView.score_opp.text =
                            getString(R.string.btn_action_ten, numActionTen.toString())
                    }
                    numActionNine++
                    popupView.catch_cargo.text =
                        getString(R.string.btn_action_nine, numActionNine.toString())
                    errorReport = 0
                    popupView.done.isEnabled = true
                }
            }
            popupView.score_opp.setOnClickListener{
                if(errorReport != 1) {
                    popupView.score_opp.text =
                        getString(R.string.btn_action_ten, numActionTen.toString())
                    popupView.score_opp.isActivated = true
                    if (errorReport == 0) {
                        popupView.catch_cargo.isActivated = false
                        numActionNine--
                        popupView.catch_cargo.text =
                            getString(R.string.btn_action_nine, numActionNine.toString())
                    }
                    numActionTen++
                    popupView.score_opp.text =
                        getString(R.string.btn_action_ten, numActionTen.toString())
                    errorReport = 1
                    popupView.done.isEnabled = true
                }
            }
            popupView.cancel.setOnClickListener{
                if(errorReport==0){
                    numActionNine--
                    popupView.catch_cargo.text = getString(R.string.btn_action_nine, numActionNine.toString())
                }
                else if(errorReport==1){
                    numActionTen--
                    popupView.score_opp.text = getString(R.string.btn_action_ten, numActionTen.toString())
                }
                popup_open = false
                enableButtons()
                popupWindow.dismiss()
            }
            popupView.done.setOnClickListener{
                if (errorReport == 0){
                    timelineAdd(match_time = match_time, action_type = Constants.ActionType.CATCH_EXIT_BALL)
                }
                else if (errorReport == 1){
                    timelineAdd(match_time = match_time, action_type = Constants.ActionType.SCORE_OPPONENT_BALL)
                }
                popup_open = false
                enableButtons()
                popupWindow.dismiss()
            }
        }

        tb_action_two.setOnClickListener {
            if (tb_action_two.isChecked) {
                goalTypeIsHigh = true
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.HIGH_LOW_TOGGLE)
                setCounterTexts()
                setBackgrounds()
                enableButtons()
            } else {
                goalTypeIsHigh = false
                timelineAdd(match_time = match_time, action_type = Constants.ActionType.HIGH_LOW_TOGGLE)
                setCounterTexts()
                setBackgrounds()
                enableButtons()
            }
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
