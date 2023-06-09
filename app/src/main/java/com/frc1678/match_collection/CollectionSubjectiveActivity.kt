// Copyright (c) 2023 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import com.frc1678.match_collection.Constants.Companion.PREVIOUS_SCREEN
import com.frc1678.match_collection.Constants.Companion.previousScreen
import com.frc1678.match_collection.Constants.Screens.COLLECTION_SUBJECTIVE
import kotlinx.android.synthetic.main.collection_subjective_activity.*
import kotlinx.android.synthetic.main.collection_subjective_activity.btn_proceed_edit
import kotlinx.android.synthetic.main.collection_subjective_activity.btn_timer
import kotlinx.android.synthetic.main.subjective_ranking_counter.view.btn_minus
import kotlinx.android.synthetic.main.subjective_ranking_counter.view.btn_plus
import kotlinx.android.synthetic.main.subjective_ranking_counter_panel.*

/**
 * Activity for Subjective Match Collection to scout the subjective gameplay of an alliance team in a match.
 */
class CollectionSubjectiveActivity : CollectionActivity() {
    private lateinit var panelOne: SubjectiveRankingCounterPanel
    private lateinit var panelTwo: SubjectiveRankingCounterPanel
    private lateinit var panelThree: SubjectiveRankingCounterPanel
    private lateinit var panelList: ArrayList<SubjectiveRankingCounterPanel>

    private lateinit var teamNumberOne: String
    private lateinit var teamNumberTwo: String
    private lateinit var teamNumberThree: String

    private var teamOneDefense: Boolean = false
    private var teamTwoDefense: Boolean = false
    private var teamThreeDefense: Boolean = false

    private var teamOneTippy: Boolean = false
    private var teamTwoTippy: Boolean = false
    private var teamThreeTippy: Boolean = false

    private var matchStarted = false
        set(value) {
            Log.d("matchStarted", "")
            field = value
            panelList.forEach {
                it.defense_toggle.isEnabled = value
                it.tippy_toggle.isEnabled = value
                it.counter_quickness.btn_minus.isEnabled = value
                it.counter_quickness.btn_plus.isEnabled = value
                it.counter_field_awareness.btn_minus.isEnabled = value
                it.counter_field_awareness.btn_plus.isEnabled = value
            }
        }

    /**
     * Finds the teams that are playing in that match
     */
    private fun getExtras() {
        teamNumberOne = intent.extras?.getString("team_one").toString()
        teamNumberTwo = intent.extras?.getString("team_two").toString()
        teamNumberThree = intent.extras?.getString("team_three").toString()
    }

    /**
     * Creates a list of teams ranked by a specific robot gameplay characteristic.
     */
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
    private val defenseToggleData: ArrayList<String>
        get() {
            val tempToggleList = arrayListOf<String>()
            for (x in 0 until panelList.size) {
                if (panelList[x].playedDefense) {
                    when (x) {
                        0 -> tempToggleList.add(teamNumberOne)
                        1 -> tempToggleList.add(teamNumberTwo)
                        2 -> tempToggleList.add(teamNumberThree)
                    }
                }
                when (x) {
                    0 -> teamOneDefense = panelList[x].playedDefense
                    1 -> teamTwoDefense = panelList[x].playedDefense
                    2 -> teamThreeDefense = panelList[x].playedDefense
                }
            }
            return tempToggleList
        }

    /**
     * Creates an Arraylist containing the teams that were tippy during the match.
     */
    private val tippyToggleData: ArrayList<String>
        get() {
            val tempToggleList = arrayListOf<String>()
            for (x in 0 until panelList.size) {
                if (panelList[x].tippy){
                    when (x) {
                        0 -> tempToggleList.add(teamNumberOne)
                        1 -> tempToggleList.add(teamNumberTwo)
                        2 -> tempToggleList.add(teamNumberThree)}
                }
                when (x) {
                    0 -> teamOneTippy = panelList[x].tippy
                    1 -> teamTwoTippy = panelList[x].tippy
                    2 -> teamThreeTippy = panelList[x].tippy
                }
            }
            return tempToggleList
        }

    /**
     * Initiate subjective_ranking_counter panels for the three teams.
     */
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
        if (previousScreen == Constants.Screens.MATCH_INFORMATION_EDIT ||
            previousScreen == Constants.Screens.QR_GENERATE
        ) {
            quicknessScore.teamOne?.let { panelOne.setQuickness(score = it.rank) }
            quicknessScore.teamTwo?.let { panelTwo.setQuickness(score = it.rank) }
            quicknessScore.teamThree?.let { panelThree.setQuickness(score = it.rank) }

            fieldAwarenessScore.teamOne?.let { panelOne.setAwareness(score = it.rank) }
            fieldAwarenessScore.teamTwo?.let { panelTwo.setAwareness(score = it.rank) }
            fieldAwarenessScore.teamThree?.let { panelThree.setAwareness(score = it.rank) }
        }

        panelOne.setAllianceColor()
        panelTwo.setAllianceColor()
        panelThree.setAllianceColor()

        panelOne.setListenerDefense()
        panelTwo.setListenerDefense()
        panelThree.setListenerDefense()

        panelOne.setListenerTippy()
        panelTwo.setListenerTippy()
        panelThree.setListenerTippy()

        panelOne.setDefense(defense = teamOneDefense)
        panelTwo.setDefense(defense = teamTwoDefense)
        panelThree.setDefense(defense = teamThreeDefense)

        panelOne.setTippy(tippy = teamOneTippy)
        panelTwo.setTippy(tippy = teamTwoTippy)
        panelThree.setTippy(tippy = teamThreeTippy)
    }

    private fun initTimer() {
        btn_timer.setOnClickListener {
            TimerUtility.MatchTimerThread().initTimer(
                this, btn_timer, btn_proceed_edit, subjective_match_collection_layout
            )
            btn_timer.isEnabled = false
            matchStarted = true
        }
    }

    // Initialize proceed button to record ranking data and proceed to MatchInformationEditActivity.kt
    // when proceed button is pressed.
    private fun initProceedButton() {
        btn_proceed_edit.setOnClickListener { view ->
            quicknessScore = recordRankingData(dataName = "quickness")
            fieldAwarenessScore = recordRankingData(dataName = "field_awareness")
            playedDefenseList = defenseToggleData
            tippyList = tippyToggleData
            defenseTimestamps = panelList.map { it.defenseTime }

            // If no robots share the same rendezvous agility and agility rankings, continue.
            // Otherwise, create error message.
            if (quicknessScore.hasDuplicate() or fieldAwarenessScore.hasDuplicate()
            ) {
                AlertDialog.Builder(this).setTitle(R.string.warning_same_rankings)
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                        goToNextActivity()
                    }.show()
            } else {
                // Add alliance teams to the intent to be used in MatchInformationEditActivity.kt.
                goToNextActivity()
            }
        }
    }

    // Begin intent used in onKeyLongPress to restart app from StartingGamePieceActivity.kt.
    private fun intentToMatchInput() {
        startActivity(
            Intent(this, StartingGamePieceActivity::class.java).putExtras(intent)
                .putExtra(PREVIOUS_SCREEN, Constants.Screens.COLLECTION_SUBJECTIVE),
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    /**
     * ends timer and displays the data if the user entered this screen by pressing the back button.
     */
    private fun comingBack() {
        if (previousScreen == Constants.Screens.MATCH_INFORMATION_EDIT ||
            previousScreen == Constants.Screens.QR_GENERATE
        ) {
            Log.d("coming-back-subjective", "came back")
            btn_proceed_edit.text = getString(R.string.btn_proceed)
            btn_proceed_edit.isEnabled = true
            btn_timer.isEnabled = false
            btn_timer.text = "0"
            teamOneDefense = intent.extras?.getBoolean("team_one_defense") ?: false
            teamTwoDefense = intent.extras?.getBoolean("team_two_defense") ?: false
            teamThreeDefense = intent.extras?.getBoolean("team_three_defense") ?: false
            teamOneTippy = intent.extras?.getBoolean("team_one_tippy") ?: false
            teamTwoTippy = intent.extras?.getBoolean("team_two_tippy") ?: false
            teamThreeTippy = intent.extras?.getBoolean("team_three_tippy") ?: false
        }
    }

    // Restart app from StartingGamePieceActivity.kt when back button is long pressed.
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
        comingBack()
        initPanels()
        matchStarted = previousScreen == Constants.Screens.MATCH_INFORMATION_EDIT
                || previousScreen == Constants.Screens.QR_GENERATE
        if (previousScreen != Constants.Screens.MATCH_INFORMATION_EDIT
            && previousScreen != Constants.Screens.QR_GENERATE
        ) {
            initTimer()

        }
        initProceedButton()
    }

    fun goToNextActivity() {
        val intent = Intent(this, MatchInformationEditActivity::class.java)
            .putExtra(PREVIOUS_SCREEN, COLLECTION_SUBJECTIVE)
            .putExtra("team_one", teamNumberOne)
            .putExtra("team_two", teamNumberTwo)
            .putExtra("team_three", teamNumberThree)
            .putExtra("team_one_defense", teamOneDefense)
            .putExtra("team_two_defense", teamTwoDefense)
            .putExtra("team_three_defense", teamThreeDefense)
            .putExtra("team_one_tippy", teamOneTippy)
            .putExtra("team_two_tippy", teamTwoTippy)
            .putExtra("team_three_tippy", teamThreeTippy)
        startActivity(
            intent, ActivityOptions.makeSceneTransitionAnimation(
                this,
                btn_proceed_edit, "proceed_button"
            ).toBundle()
        )
    }
}
