// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import kotlinx.android.synthetic.main.collection_subjective_activity.*

// Activity for Subjective Match Collection to scout the subjective gameplay of an alliance team in a match.
class CollectionSubjectiveActivity : CollectionActivity() {
    private lateinit var panelOne: SubjectiveRankingCounterPanel
    private lateinit var panelTwo: SubjectiveRankingCounterPanel
    private lateinit var panelThree: SubjectiveRankingCounterPanel

    private lateinit var panelList: ArrayList<SubjectiveRankingCounterPanel>

    private lateinit var teamNumberOne: String
    private lateinit var teamNumberTwo: String
    private lateinit var teamNumberThree: String

    private fun getExtras() {
        teamNumberOne = intent.extras?.getString("team_one").toString()
        teamNumberTwo = intent.extras?.getString("team_two").toString()
        teamNumberThree = intent.extras?.getString("team_three").toString()
    }

    // Create list of teams ranked by a specific robot gameplay characteristic.
    private fun recordRankingData(dataName: String): SubjectiveTeamRankings {
        val panelOneData = panelOne.rankingData[dataName]
        val panelTwoData = panelTwo.rankingData[dataName]
        val panelThreeData = panelThree.rankingData[dataName]
        return SubjectiveTeamRankings(
            TeamRank(teamNumberOne, panelOneData ?: SubjectiveRankingCounter.startingValue),
            TeamRank(teamNumberTwo, panelTwoData ?: SubjectiveRankingCounter.startingValue),
            TeamRank(teamNumberThree, panelThreeData ?: SubjectiveRankingCounter.startingValue)
        )
    }

    /**
     * Creates an ArrayList containing the teams that played defense during the match.
     */
    private val defenseToggleData: ArrayList<String> get() {
        val tempToggleList = arrayListOf<String>()
        for (x in 0 until panelList.size) {
            if (panelList[x].playedDefense) {
                when (x) {
                    0 -> tempToggleList.add(teamNumberOne)
                    1 -> tempToggleList.add(teamNumberTwo)
                    2 -> tempToggleList.add(teamNumberThree)
                }
            }
        }
        return tempToggleList
    }

    // Initiate subjective_ranking_counter panels for the three teams.
    private fun initPanels() {
        panelOne =
            supportFragmentManager.findFragmentById(R.id.robotOne) as SubjectiveRankingCounterPanel
        panelTwo =
            supportFragmentManager.findFragmentById(R.id.robotTwo) as SubjectiveRankingCounterPanel
        panelThree =
            supportFragmentManager.findFragmentById(R.id.robotThree) as SubjectiveRankingCounterPanel

        panelList =
            arrayListOf(panelOne, panelTwo, panelThree)

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
            quickness_score = recordRankingData(dataName = "Quickness")
            field_awareness_score = recordRankingData(dataName = "Field Aware")
            played_defense_list = defenseToggleData

            // If no robots share the same rendezvous agility and agility rankings, continue.
            // Otherwise, create error message.
            if (quickness_score.hasDuplicate() or field_awareness_score.hasDuplicate()
            ) {
                AlertDialog.Builder(this).setTitle(R.string.warning_same_rankings)
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }.setPositiveButton("Proceed") { _: DialogInterface, _: Int ->
                        goToNextActivity()
                    }.show()
            } else {
                // Add alliance teams to the intent to be used in MatchInformationEditActivity.kt.
                goToNextActivity()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_subjective_activity)

        getExtras()
        initProceedButton()
        initPanels()
    }

    fun goToNextActivity() {
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
