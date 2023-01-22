// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.os.CountDownTimer

// File to store information to be used to create the final match information map.
var numActionOne = 0 //INTAKE_STATION
var numActionTwo = 0 //INTAKE_LOW_ROW
var numActionThree = 0 //INTAKE_GROUND
var numActionFour = 0 //SCORE_CUBE_HIGH
var numActionFive = 0 //SCORE_CUBE_MID
var numActionSix = 0 //SCORE_CUBE_LOW
var numActionSeven = 0 //SCORE_CONE_HIGH
var numActionEight = 0 //SCORE_CONE_MID
var numActionNine = 0 //SCORE_CONE_LOW
var numActionTen = 0 //FAIL
var autoIntakeGamePieceOne: Boolean = false
var autoIntakeGamePieceTwo: Boolean = false
var autoIntakeGamePieceThree: Boolean= false
var autoIntakeGamePieceFour: Boolean= false


var match_timer: CountDownTimer? = null
var match_time: String = ""
var is_teleop_activated: Boolean = false
var popup_open = false
var is_match_time_ended: Boolean = false
var collection_mode: Constants.ModeSelection = Constants.ModeSelection.NONE
var assign_mode: Constants.AssignmentMode = Constants.AssignmentMode.NONE
var did_auto_charge: Boolean = false
var did_tele_charge: Boolean = false

// Data that is shared between the objective and subjective QRs.
var serial_number: String? = ""
var match_number: Int = 0
var alliance_color: Constants.AllianceColor = Constants.AllianceColor.NONE
var timestamp: Long = 0
var match_collection_version_number: String = "1.0.0"
var scout_name: String = Constants.NONE_VALUE

// Data specific to Objective Match Collection QR.
var team_number: String = ""
var scout_id: String = Constants.NONE_VALUE
var orientation: Boolean = true //true = UP, false = DOWN
var starting_position: Constants.StartingPosition = Constants.StartingPosition.NONE
var preloaded: Constants.Preloaded = Constants.Preloaded.NONE
var timeline = mutableListOf<Map<String, String>>()
var auto_charge_level: Constants.ChargeLevel = Constants.ChargeLevel.NONE
var tele_charge_level: Constants.ChargeLevel = Constants.ChargeLevel.NONE

// Data specific to Subjective Match Collection QR.
var quickness_score: SubjectiveTeamRankings = SubjectiveTeamRankings()
var field_awareness_score: SubjectiveTeamRankings = SubjectiveTeamRankings()
var intake_cone_orientation_list: ArrayList<String> = ArrayList()
var played_defense_list: ArrayList<String> = ArrayList()
var gamePiecePositionList = mutableListOf(Constants.GamePiecePositions.NONE,
    Constants.GamePiecePositions.NONE, Constants.GamePiecePositions.NONE, Constants.GamePiecePositions.NONE)

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
    autoIntakeGamePieceOne = false
    autoIntakeGamePieceTwo = false
    autoIntakeGamePieceThree = false
    autoIntakeGamePieceFour = false

    is_teleop_activated = false
    did_auto_charge = false
    did_tele_charge = false

    popup_open = false
    auto_charge_level = Constants.ChargeLevel.NONE
    tele_charge_level = Constants.ChargeLevel.NONE

    timestamp = 0

    timeline = ArrayList()

    quickness_score = SubjectiveTeamRankings()
    field_awareness_score = SubjectiveTeamRankings()
    intake_cone_orientation_list = ArrayList()
    played_defense_list = ArrayList()
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
    starting_position = Constants.StartingPosition.NONE
    for (x in 0..3) {
        gamePiecePositionList[x] = Constants.GamePiecePositions.NONE
    }
    preloaded = Constants.Preloaded.NONE
    team_number = ""
}
