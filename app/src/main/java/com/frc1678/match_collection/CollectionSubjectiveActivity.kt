// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.collection_objective_activity.*
import kotlinx.android.synthetic.main.collection_subjective_activity.*
import kotlinx.android.synthetic.main.collection_subjective_activity.btn_proceed_edit

// Activity for Subjective Match Collection to scout the subjective gameplay of an alliance team in a match.
class CollectionSubjectiveActivity : CollectionActivity() {
    private lateinit var panelOne: SubjectiveRankingCounterPanel
    private lateinit var panelTwo: SubjectiveRankingCounterPanel
    private lateinit var panelThree: SubjectiveRankingCounterPanel

    private lateinit var teamNumberOne: String
    private lateinit var teamNumberTwo: String
    private lateinit var teamNumberThree: String

    private var numHumanShots = 0

    private fun getExtras() {
        teamNumberOne = intent.extras?.getString("team_one").toString()
        teamNumberTwo = intent.extras?.getString("team_two").toString()
        teamNumberThree = intent.extras?.getString("team_three").toString()
    }

    // Create list of teams ranked by a specific robot gameplay characteristic.
    private fun recordRankingData(dataName: String): ArrayList<String> {
        val tempRankingList: ArrayList<String> = arrayListOf("rankOne", "rankTwo", "rankThree")

        tempRankingList[panelOne.getRankingData().getValue(dataName) - 1] = teamNumberOne
        tempRankingList[panelTwo.getRankingData().getValue(dataName) - 1] = teamNumberTwo
        tempRankingList[panelThree.getRankingData().getValue(dataName) - 1] = teamNumberThree

        return tempRankingList
    }

    // Initiate subjective_ranking_counter panels for the three teams.
    private fun initPanels() {
        panelOne =
            supportFragmentManager.findFragmentById(R.id.robotOne) as SubjectiveRankingCounterPanel
        panelTwo =
            supportFragmentManager.findFragmentById(R.id.robotTwo) as SubjectiveRankingCounterPanel
        panelThree =
            supportFragmentManager.findFragmentById(R.id.robotThree) as SubjectiveRankingCounterPanel

        panelOne.setTeamNumber(teamNumber = teamNumberOne)
        panelTwo.setTeamNumber(teamNumber = teamNumberTwo)
        panelThree.setTeamNumber(teamNumber = teamNumberThree)

        panelOne.setAllianceColor()
        panelTwo.setAllianceColor()
        panelThree.setAllianceColor()
    }

    // Initialize proceed button to record ranking data and proceed to MatchInformationEditActivity.kt
    // when proceed button is pressed.
    private fun initProceedButton() {
        btn_proceed_edit.setOnClickListener { view ->
            quickness_rankings = recordRankingData(dataName = "Quickness")
            driver_field_awareness_far_rankings = recordRankingData(dataName = "Far Field Awareness")
            driver_field_awareness_near_rankings = recordRankingData(dataName = "Near Field Awareness")

            // If no robots share the same rendezvous agility and agility rankings, continue.
            // Otherwise, create error message.
            if (quickness_rankings.toString().contains("rank") or driver_field_awareness_far_rankings.toString().contains("rank") or driver_field_awareness_near_rankings.toString().contains("rank")) {
                createErrorMessage(message = getString(R.string.error_same_rankings), view = view)
            } else {
                // Add alliance teams to the intent to be used in MatchInformationEditActivity.kt.
                val intent = Intent(this, MatchInformationEditActivity::class.java)
                intent.putExtra("team_one", teamNumberOne)
                    .putExtra("team_two", teamNumberTwo)
                    .putExtra("team_three", teamNumberThree)
                startActivity(
                    intent, ActivityOptions.makeSceneTransitionAnimation(
                        this,
                        btn_proceed_edit, "proceed_button"
                    ).toBundle()
                )
            }
        }
    }

    // Begin intent used in onKeyLongPress to restart app from MatchInformationInputActivity.kt.
    private fun intentToMatchInput() {
        startActivity(
            Intent(this, MatchInformationInputActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    // Restart app from MatchInformationInputActivity.kt when back button is long pressed.
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> intentToMatchInput() }
                .show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    private fun initHumanShotsButton() {

        btn_human_shots.text = getString(R.string.human_shots, numHumanShots.toString())

        if (alliance_color == Constants.AllianceColor.RED) {
            btn_human_shots.setBackgroundResource(R.drawable.btn_human_player_red_selector)
        } else {
            btn_human_shots.setBackgroundResource(R.drawable.btn_human_player_blue_selector)
        }

        btn_human_shots.setOnClickListener {
            numHumanShots++
            btn_human_shots.text = getString(R.string.human_shots, numHumanShots.toString())
        }

        btn_human_shots.setOnLongClickListener(View.OnLongClickListener {
            numHumanShots--
            btn_human_shots.text = getString(R.string.human_shots, numHumanShots.toString())
            return@OnLongClickListener true
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_subjective_activity)

        getExtras()
        initProceedButton()
        initPanels()
        initHumanShotsButton()
    }
}
