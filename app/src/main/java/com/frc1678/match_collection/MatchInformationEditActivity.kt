// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import com.frc1678.match_collection.CollectionObjectiveActivity.Companion.comingBack
import kotlinx.android.synthetic.main.edit_match_information_activity.*
import java.lang.Integer.parseInt

// Class to edit previously inputted match information.
class MatchInformationEditActivity : MatchInformationActivity() {
    private lateinit var teamNumberOne: String
    private lateinit var teamNumberTwo: String
    private lateinit var teamNumberThree: String

    // Get subjective team numbers through intent extras from CollectionSubjectiveActivity.kt.
    private fun getExtras() {
        teamNumberOne = intent.extras?.getString("team_one").toString()
        teamNumberTwo = intent.extras?.getString("team_two").toString()
        teamNumberThree = intent.extras?.getString("team_three").toString()
    }

    // Populate edit texts with previously inputted match information data.
    private fun populateData() {
        et_match_number.setText(match_number.toString())
        if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
            et_team_one.setText(team_number)
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
        match_number = parseInt(et_match_number.text.toString())
        if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
            team_number = et_team_one.text.toString()
        } else {
            for (ranking in listOf(quickness_score, field_awareness_score)) {
                Log.d("match-information-edit", quickness_score.toString())
                Log.d("match-information-edit", field_awareness_score.toString())

                // Sets the teams for quickness, field awareness, and defense based on the team
                // numbers being displayed
                ranking.teamOne?.teamNumber = et_team_one.text.toString()
                ranking.teamTwo?.teamNumber = et_team_two.text.toString()
                ranking.teamThree?.teamNumber = et_team_three.text.toString()
            }
            if (played_defense_list.contains(teamNumberOne)) {
                played_defense_list[played_defense_list.indexOf(teamNumberOne)] =
                    et_team_one.text.toString()
            }
            if (played_defense_list.contains(teamNumberTwo)) {
                played_defense_list[played_defense_list.indexOf(teamNumberTwo)] =
                    et_team_two.text.toString()
            }
            if (played_defense_list.contains(teamNumberThree)) {
                played_defense_list[played_defense_list.indexOf(teamNumberThree)] =
                    et_team_three.text.toString()
            }

        }
    }

    // Call updateMatchInformation() and proceed to QRGenerateActivity.kt.
    private fun generateQR() {
        updateMatchInformation()

        val intent = Intent(this, QRGenerateActivity::class.java)
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


    // Restart app from MatchInformationInputActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean{
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(starting_position.toString() != "ZERO"){
                comingBack = "match information edit"
                startActivity(
                    Intent(this, CollectionObjectiveActivity::class.java)
                )
            }
            else{
                startActivity(
                    Intent(this, StartingPositionObjectiveActivity::class.java)
                )
            }
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_match_information_activity)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        populateData()
        initScoutNameSpinner(context = this, spinner = spinner_scout_name)
        initProceedButton()
    }
}
