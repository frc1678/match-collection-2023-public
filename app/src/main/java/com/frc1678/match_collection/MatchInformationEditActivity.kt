// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import com.frc1678.match_collection.Constants.Companion.PREVIOUS_SCREEN
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
            if (scoredCoopList.contains(teamNumberOne)){
                scoredCoopList[scoredCoopList.indexOf(teamNumberOne)] =
                    et_team_one.text.toString()
            }
            if (scoredCoopList.contains(teamNumberTwo)){
                scoredCoopList[scoredCoopList.indexOf(teamNumberTwo)] =
                    et_team_two.text.toString()
            }
            if (scoredCoopList.contains(teamNumberThree)){
                scoredCoopList[scoredCoopList.indexOf(teamNumberThree)] =
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
            .putExtra("previousScreen","collection")
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
            if(collectionMode == Constants.ModeSelection.OBJECTIVE){
                if(startingPosition.toString() != "ZERO"){
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
                        .putExtra(PREVIOUS_SCREEN, Constants.Screens.MATCH_INFORMATION_EDIT)
                )
            }
        }
        return super.onKeyLongPress(keyCode, event)
    }

    // Sets the edit match information activity screen the populates it with previously inputted data
    // Initiates the scout name dropdown so that users can change their scout name if they need
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_match_information_activity)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        populateData()
        initScoutNameSpinner(context = this, spinner = spinner_scout_name)
        initProceedButton()
    }
}
