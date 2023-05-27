// Copyright (c) 2023 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.frc1678.match_collection.Constants.Companion.PREVIOUS_SCREEN
import kotlinx.android.synthetic.main.edit_match_information_activity.*
import kotlinx.android.synthetic.main.edit_match_information_activity.et_match_number
import kotlinx.android.synthetic.main.edit_match_information_activity.et_team_one
import kotlinx.android.synthetic.main.edit_match_information_activity.spinner_scout_name
import kotlinx.android.synthetic.main.match_information_input_activity_objective.*
import java.lang.Integer.parseInt

// Class to edit previously inputted match information.
class MatchInformationEditActivity : MatchInformationActivity() {
    private lateinit var teamNumberOne: String
    private lateinit var teamNumberTwo: String
    private lateinit var teamNumberThree: String

    private var blueToggleButtonColor: Int = 0
    private var redToggleButtonColor: Int = 0
    private var blueToggleButtonColorDark: Int = 0
    private var redToggleButtonColorDark: Int = 0

    private lateinit var blueToggleButton: Button
    private lateinit var redToggleButton: Button

    // Get subjective team numbers through intent extras from CollectionSubjectiveActivity.kt.
    private fun getExtras() {
        teamNumberOne = intent.extras?.getString("team_one").toString()
        teamNumberTwo = intent.extras?.getString("team_two").toString()
        teamNumberThree = intent.extras?.getString("team_three").toString()
    }

    // Populate edit texts with previously inputted match information data.
    private fun populateData() {
        et_match_number.setText(matchNumber.toString())
        if (collectionMode == Constants.ModeSelection.OBJECTIVE) {
            et_team_one.setText(teamNumber)
        } else {
            getExtras()

            makeViewVisible(et_team_two, et_team_three, tv_hint_team_two, tv_hint_team_three)
            makeViewInvisible(separator_team_num_spinner)

            et_team_one.setText(teamNumberOne)
            et_team_two.setText(teamNumberTwo)
            et_team_three.setText(teamNumberThree)
        }
    }

    // Update match information based on newly inputted information.
    private fun updateMatchInformation() {
        matchNumber = parseInt(et_match_number.text.toString())
        if (collectionMode == Constants.ModeSelection.OBJECTIVE) {
            teamNumber = et_team_one.text.toString()
        } else {
            for (ranking in listOf(quicknessScore, fieldAwarenessScore)) {
                Log.d("match-information-edit", quicknessScore.toString())
                Log.d("match-information-edit", fieldAwarenessScore.toString())

                // Sets the teams for quickness, field awareness, and defense based on the team
                // numbers being displayed
                ranking.teamOne?.teamNumber = et_team_one.text.toString()
                ranking.teamTwo?.teamNumber = et_team_two.text.toString()
                ranking.teamThree?.teamNumber = et_team_three.text.toString()
            }
            if (tippyList.contains(teamNumberOne)){
                tippyList[tippyList.indexOf(teamNumberOne)] =
                    et_team_one.text.toString()
            }
            if (tippyList.contains(teamNumberTwo)){
                tippyList[tippyList.indexOf(teamNumberTwo)] =
                    et_team_two.text.toString()
            }
            if (tippyList.contains(teamNumberThree)){
                tippyList[tippyList.indexOf(teamNumberThree)] =
                    et_team_three.text.toString()
            }

            if (playedDefenseList.contains(teamNumberOne)) {
                playedDefenseList[playedDefenseList.indexOf(teamNumberOne)] =
                    et_team_one.text.toString()
            }
            if (playedDefenseList.contains(teamNumberTwo)) {
                playedDefenseList[playedDefenseList.indexOf(teamNumberTwo)] =
                    et_team_two.text.toString()
            }
            if (playedDefenseList.contains(teamNumberThree)) {
                playedDefenseList[playedDefenseList.indexOf(teamNumberThree)] =
                    et_team_three.text.toString()
            }
        }
    }

    // Call updateMatchInformation() and proceed to QRGenerateActivity.kt.
    private fun generateQR() {
        updateMatchInformation()

        val intent = Intent(this, QRGenerateActivity::class.java).putExtras(intent)
            .putExtra(PREVIOUS_SCREEN, Constants.Screens.MATCH_INFORMATION_EDIT)
        startActivity(
            intent, ActivityOptions.makeSceneTransitionAnimation(
                this,
                btn_proceed_qr_generate, "proceed_button"
            ).toBundle()
        )
    }

    // Initialize proceed button to set the updated values and start QRGenerateActivity.kt.
    private fun initProceedButton() {
        btn_proceed_qr_generate.setOnClickListener { view ->
            if (safetyCheck(view = view)) {
                generateQR()
            }
        }
    }

    // Begin intent used in onKeyLongPress to restart app from MatchInformationInputActivity.kt.
    private fun intentToMatchInput() {
    }


    // Goes back to collectionObjectiveActivity if in objective mode
    // Restart app from MatchInformationInputActivity.kt when back button is long pressed if in Subjective
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean{
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            updateMatchInformation()
            if(collectionMode == Constants.ModeSelection.OBJECTIVE){
                if(startingPosition.toString() != "0"){
                    teamNumber = et_team_one.text.toString()
                    startActivity(
                        Intent(this, CollectionObjectiveActivity::class.java)
                            .putExtra(PREVIOUS_SCREEN, Constants.Screens.MATCH_INFORMATION_EDIT)
                    )
                }
                else{
                    startActivity(
                        Intent(this, StartingPositionObjectiveActivity::class.java)
                            .putExtra(PREVIOUS_SCREEN, Constants.Screens.MATCH_INFORMATION_EDIT)
                    )
                }
            }
            else if(collectionMode == Constants.ModeSelection.SUBJECTIVE){
                startActivity(
                    Intent(this, CollectionSubjectiveActivity::class.java).putExtras(intent)
                        .putExtra("team_one",et_team_one.text.toString())
                        .putExtra("team_two",et_team_two.text.toString())
                        .putExtra("team_three",et_team_three.text.toString())
                        .putExtra(PREVIOUS_SCREEN, Constants.Screens.MATCH_INFORMATION_EDIT)
                )
            }
        }
        return super.onKeyLongPress(keyCode, event)
    }

    // Only lets the user type in numbers and uppercase letters
    private fun initTeamNumberTextChangeListeners() {
        val regex = "[^A-Z0-9]".toRegex()
        et_team_one.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (checkInputNotEmpty(et_team_one)) {
                    if (s.toString().contains(regex)) {
                        val tempString: String = et_team_one.text.toString()
                        et_team_one.setText(regex.replace(tempString,""))
                    }
                }
            }
        })
        if (collectionMode == Constants.ModeSelection.SUBJECTIVE) {
            et_team_two.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (checkInputNotEmpty(et_team_one)) {
                        if (s.toString().contains(regex)) {
                            val tempString: String = et_team_two.text.toString()
                            et_team_two.setText(regex.replace(tempString,""))
                        }
                    }
                }
            })
            et_team_three.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (checkInputNotEmpty(et_team_one)) {
                        if (s.toString().contains(regex)) {
                            val tempString: String = et_team_three.text.toString()
                            et_team_three.setText(regex.replace(tempString,""))
                        }
                    }
                }
            })
        }
    }

    // Create an alliance color toggle button given its specifications.
    private fun createToggleButton(
        isBordered: Boolean, toggleButton: Button,
        toggleButtonColor: Int, toggleButtonColorDark: Int
    ) {
        val backgroundDrawable = GradientDrawable()

        if (isBordered) {
            backgroundDrawable.setStroke(10, toggleButtonColorDark)
        }

        backgroundDrawable.setColor(toggleButtonColor)
        backgroundDrawable.cornerRadius = 10f
        toggleButton.background = backgroundDrawable
    }

    private fun resetBackground() {
        // Create the unselected alliance color left toggle.
        createToggleButton(
            isBordered = false,
            toggleButton = blueToggleButton,
            toggleButtonColor = blueToggleButtonColor,
            toggleButtonColorDark = blueToggleButtonColorDark
        )

        // Create the unselected alliance color right toggle.
        createToggleButton(
            isBordered = false,
            toggleButton = redToggleButton,
            toggleButtonColor = redToggleButtonColor,
            toggleButtonColorDark = redToggleButtonColorDark
        )
    }

    private fun initToggleButtons() {
        redToggleButtonColor = ContextCompat.getColor(this, R.color.alliance_red_light)
        blueToggleButtonColor = ContextCompat.getColor(this, R.color.alliance_blue_light)
        redToggleButtonColorDark = ContextCompat.getColor(this, R.color.alliance_red_dark)
        blueToggleButtonColorDark = ContextCompat.getColor(this, R.color.alliance_blue_dark)
        redToggleButton = red_toggle_button
        blueToggleButton = blue_toggle_button

        resetBackground()

        when (allianceColor) {
            Constants.AllianceColor.BLUE -> {
                switchBorderToBlueToggle()
            }
            else -> {
                switchBorderToRedToggle()
            }
        }

        blueToggleButton.setOnLongClickListener {
            allianceColor = Constants.AllianceColor.BLUE
            switchBorderToBlueToggle()
            return@setOnLongClickListener true
        }
        redToggleButton.setOnLongClickListener {
            allianceColor = Constants.AllianceColor.RED
            switchBorderToRedToggle()
            return@setOnLongClickListener true
        }
    }

    // Apply border to red alliance toggle when red alliance selected.
    private fun switchBorderToRedToggle() {
        resetBackground()

        // Create selected red toggle.
        createToggleButton(
            isBordered = true,
            toggleButton = redToggleButton,
            toggleButtonColor = redToggleButtonColor,
            toggleButtonColorDark = redToggleButtonColorDark
        )
    }

    // Apply border to blue alliance toggle when blue alliance is selected.
    private fun switchBorderToBlueToggle() {
        resetBackground()

        // Create selected blue toggle.
        createToggleButton(
            isBordered = true,
            toggleButton = blueToggleButton,
            toggleButtonColor = blueToggleButtonColor,
            toggleButtonColorDark = blueToggleButtonColorDark
        )
    }

    // Sets the edit match information activity screen the populates it with previously inputted data
    // Initiates the scout name dropdown so that users can change their scout name if they need
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_match_information_activity)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        populateData()
        initToggleButtons()
        initTeamNumberTextChangeListeners()
        initScoutNameSpinner(context = this, spinner = spinner_scout_name)
        initProceedButton()
        if (collectionMode == Constants.ModeSelection.OBJECTIVE) {
            tv_team_one.text = "Team Number"
        }
    }
}
