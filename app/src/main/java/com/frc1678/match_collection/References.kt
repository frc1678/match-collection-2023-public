// Copyright (c) 2023 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// File to store information to be used to create the final match information map.
var numActionOne = 0 //INTAKE_DOUBLE
var numActionTwo = 0 //INTAKE_HIGH_ROW
var numActionThree = 0 //INTAKE_MID_ROW
var numActionFour = 0 //INTAKE_LOW_ROW
var numActionFive = 0 //INTAKE_GROUND
var numActionSix = 0 //SCORE_CUBE_HIGH
var numActionSeven = 0 //SCORE_CUBE_MID
var numActionEight = 0 //SCORE_CUBE_LOW
var numActionNine = 0 //SCORE_CONE_HIGH
var numActionTen = 0 //SCORE_CONE_MID
var numActionEleven = 0 //SCORE_CONE_LOW
var numActionTwelve = 0 //SCORE_FAIL
var numActionThirteen = 0 //INTAKE_SINGLE
var numActionFourteen = 0 //SUPERCHARGE

var autoIntakeGamePieceOne = 0
var autoIntakeGamePieceTwo = 0
var autoIntakeGamePieceThree = 0
var autoIntakeGamePieceFour = 0


var matchTimer: CountDownTimer? = null
var matchTime: String = ""
var isTeleopActivated: Boolean = false
var popupOpen = false
var isMatchTimeEnded: Boolean = false
var collectionMode: Constants.ModeSelection = Constants.ModeSelection.NONE
var assignMode: Constants.AssignmentMode = Constants.AssignmentMode.NONE
var didAutoCharge: Boolean = false
var didTeleCharge: Boolean = false
var didAutoFail: Boolean = false
var didTeleFail: Boolean = false

// Data that is shared between the objective and subjective QRs.
var serialNumber: String? = ""
var matchNumber: Int = 0
var allianceColor: Constants.AllianceColor = Constants.AllianceColor.NONE
var timestamp: Long = 0
var scoutName: String = Constants.NONE_VALUE

// Data specific to Objective Match Collection QR.
var teamNumber: String = ""
var scoutId: String = Constants.NONE_VALUE
var orientation by mutableStateOf(true)
var startingPosition: Int? = null
var preloaded: Constants.Preloaded = Constants.Preloaded.N
var timeline = mutableListOf<Map<String, String>>()
var autoChargeLevel: Constants.ChargeLevel = Constants.ChargeLevel.N
var teleChargeLevel: Constants.ChargeLevel = Constants.ChargeLevel.N
var prevAutoChargeLevel: Constants.ChargeLevel = Constants.ChargeLevel.N
var prevTeleChargeLevel: Constants.ChargeLevel = Constants.ChargeLevel.N

// Data specific to Subjective Match Collection QR.
var quicknessScore: SubjectiveTeamRankings = SubjectiveTeamRankings()
var fieldAwarenessScore: SubjectiveTeamRankings = SubjectiveTeamRankings()
var tippyList: ArrayList<String> = ArrayList()
var playedDefenseList: ArrayList<String> = ArrayList()
var gamePiecePositionList = List(4) { Constants.GamePiecePositions.N }
var defenseTimestamps = listOf<Int?>(null, null, null)

// Function to reset References.kt variables for new match.
fun resetCollectionReferences() {
    numActionOne = 0
    numActionTwo = 0
    numActionThree = 0
    numActionFour = 0
    numActionFive = 0
    numActionSix = 0
    numActionSeven = 0
    numActionEight = 0
    numActionNine = 0
    numActionTen = 0
    numActionEleven = 0
    numActionTwelve = 0
    numActionThirteen = 0
    numActionFourteen = 0
    autoIntakeGamePieceOne = 0
    autoIntakeGamePieceTwo = 0
    autoIntakeGamePieceThree = 0
    autoIntakeGamePieceFour = 0

    isTeleopActivated = false
    didAutoCharge = false
    didTeleCharge = false
    didAutoFail = false
    didTeleFail = false

    popupOpen = false
    autoChargeLevel = Constants.ChargeLevel.N
    teleChargeLevel = Constants.ChargeLevel.N
    prevAutoChargeLevel = Constants.ChargeLevel.N
    prevTeleChargeLevel = Constants.ChargeLevel.N

    timestamp = 0

    timeline = ArrayList()

    quicknessScore = SubjectiveTeamRankings()
    fieldAwarenessScore = SubjectiveTeamRankings()
    tippyList = ArrayList()
    playedDefenseList = ArrayList()
    defenseTimestamps = listOf(null, null, null)
}

data class SubjectiveTeamRankings(
    val teamOne: TeamRank? = null,
    val teamTwo: TeamRank? = null,
    val teamThree: TeamRank? = null
) {
    val list: List<TeamRank?>
        get() = listOf(teamOne, teamTwo, teamThree)

    val notNullList: List<TeamRank>
        get() = this.list.filterNotNull()


    fun hasDuplicate(): Boolean {
        val ranks = mutableListOf<Int>()
        for (team in this.notNullList) {
            ranks.add(team.rank)
        }
        return ranks.toSet().toList() != ranks
    }
}

data class TeamRank(var teamNumber: String, val rank: Int)

fun resetStartingReferences() {
    startingPosition = null
    gamePiecePositionList = List(4) { Constants.GamePiecePositions.N }
    preloaded = Constants.Preloaded.N
    teamNumber = ""
}
