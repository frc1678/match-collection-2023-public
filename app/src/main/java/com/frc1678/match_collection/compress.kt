// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.util.Log
import java.util.*

// Function to create compressed string displayed in QR.
// String takes collected data stored in References.kt compressed by Match Collection schema.
fun compress(
    schema: HashMap<String, HashMap<String, Any>>
): String {
    var compressedMatchInformation: String

    val schemaVersion = schema.getValue("schema_file").getValue("version").toString()

    // Define HashMaps for categories of data based on match_collection_qr_schema.yml.
    val genericData = schema.getValue("generic_data")
    val objectiveData = schema.getValue("objective_tim")
    val subjectiveData = schema.getValue("subjective_aim")
    val actionTypeData = schema.getValue("action_type")

    // Define compression characters for generic separators.
    val genericSeparator = genericData.getValue("_separator").toString()
    val genericSectionSeparator = genericData.getValue("_section_separator").toString()
    // Define compression characters for generic data.
    val compressSchemaVersion = genericData.getValue("schema_version").toString().split(",")[0]
    val compressSerialNumber = genericData.getValue("serial_number").toString().split(",")[0]
    val compressMatchNumber = genericData.getValue("match_number").toString().split(",")[0]
    val compressTimestamp = genericData.getValue("timestamp").toString().split(",")[0]
    val compressVersionNum =
        genericData.getValue("match_collection_version_number").toString().split(",")[0]
    val compressScoutName = genericData.getValue("scout_name").toString().split(",")[0]
    val compressAllianceColor =
        genericData.getValue("alliance_color_is_red").toString().split(",")[0]

    // Define compression characters for objective separators.
    val objectiveStartCharacter = objectiveData.getValue("_start_character").toString()
    val objectiveSeparator = objectiveData.getValue("_separator").toString()
    // Define compression characters for objective data.
    val compressTeamNumber = objectiveData.getValue("team_number").toString().split(",")[0]
    val compressScoutId = objectiveData.getValue("scout_id").toString().split(",")[0]
    val compressStartingPosition = objectiveData.getValue("start_position").toString().split(",")[0]
    val compressTimeline = objectiveData.getValue("timeline").toString().split(",")[0]
    val compressEndgame = objectiveData.getValue("climb_level").toString().split(",")[0]
    val compressPreloaded = objectiveData.getValue("preloaded_piece").toString().split(",")[0]

    // Define compression characters for subjective separators.
    val subjectiveStartCharacter = subjectiveData.getValue("_start_character").toString()
    val subjectiveSeparator = subjectiveData.getValue("_separator").toString()
    val subjectiveTeamSeparator = subjectiveData.getValue("_team_separator").toString()
    val subjectiveTeamNumberSeparator =
        subjectiveData.getValue("team_number").toString().split(",")[0]
    // Define compression characters for subjective data.
    val compressQuicknessScore = subjectiveData.getValue("quickness_score").toString().split(",")[0]
    val compressAwareScore =
        subjectiveData.getValue("field_awareness_score").toString().split(",")[0]
    val compressPlayedDefense = subjectiveData.getValue("played_defense").toString().split(",")[0]
    val compressGamePiece = subjectiveData.getValue("game_piece").toString().split(",")[0]
    val compressIntakeConeOrientation = subjectiveData.getValue("intake_cone_orientation").toString().split(",")[0]

    // Compress and add data shared between the objective and subjective modes.
    compressedMatchInformation =
        compressSchemaVersion + schemaVersion + genericSeparator +
                compressSerialNumber + serial_number + genericSeparator +
                compressMatchNumber + match_number + genericSeparator +
                compressTimestamp + timestamp + genericSeparator +
                compressVersionNum + match_collection_version_number + genericSeparator +
                compressScoutName + scout_name.toUpperCase(Locale.US) + genericSeparator +
                compressAllianceColor + if (alliance_color == Constants.AllianceColor.RED) "TRUE" else "FALSE"

    // Compress and add data specific to Objective Match Collection.
    if (collection_mode == Constants.ModeSelection.OBJECTIVE) {
        // Compress timeline actions if timeline exists.
        var compressTimelineActions = ""
        if (timeline.isNotEmpty()) {
            for (actions in timeline) {
                // Compress and add timeline action attributes present for all actions.
                compressTimelineActions = compressTimelineActions +
                        actions.getValue("match_time") + actionTypeData.getValue(
                    actions.getValue("action_type").toString().toLowerCase(
                        Locale.US
                    )
                )
            }
        }

        // Compress and add all Objective Match Collection data, including previously compressed
        // timeline actions.
        compressedMatchInformation =
            objectiveStartCharacter + compressedMatchInformation + genericSectionSeparator +
                    compressTeamNumber + team_number + objectiveSeparator +
                    compressScoutId + scout_id + objectiveSeparator +
                    compressStartingPosition + starting_position.toString() + objectiveSeparator +
                    compressTimeline + compressTimelineActions + objectiveSeparator +
                    compressEndgame + climb_level + objectiveSeparator +
                    compressPreloaded + preloaded
    }
    // Compress and add data specific to Subjective Match Collection.
    else if (collection_mode == Constants.ModeSelection.SUBJECTIVE) {
        var subjDataString = ""
        val teamNumbers = subjectiveTeamRankingsToList(quickness_score)

        teamNumbers.forEachIndexed { i, teamNum ->
            subjDataString += subjectiveTeamNumberSeparator
            subjDataString += teamNum
            val quickness = getRankForTeam(quickness_score, teamNum)
            val fieldAwareness = getRankForTeam(field_awareness_score, teamNum)
            val playedDefense = played_defense_list.contains(teamNum)
            val intakeConeOrientation = intake_cone_orientation_list.contains(teamNum)
            var gamePiece = " "

            // Goes through all the game pieces and checks if they are either a cone or cube
            // if it is a cone, then adds 0 to gamePiece, 1 if it is a cube
            for (x in 0..3) {
                if(gamePiecePositionList[x] == Constants.GamePiecePositions.CONE) {
                    gamePiece += "0"
                } else if (gamePiecePositionList[x] == Constants.GamePiecePositions.CUBE) {
                    gamePiece +="1"
                }
                // Shouldn't ever happen
                else {
                    gamePiece += "2"
                }
            }

            subjDataString += subjectiveSeparator
            subjDataString += compressQuicknessScore
            subjDataString += quickness.toString()

            subjDataString += subjectiveSeparator
            subjDataString += compressAwareScore
            subjDataString += fieldAwareness.toString()

            subjDataString += subjectiveSeparator
            subjDataString += compressPlayedDefense
            subjDataString += if (playedDefense) "TRUE" else "FALSE"

            subjDataString += subjectiveSeparator
            subjDataString += compressGamePiece
            subjDataString += gamePiece
            
            subjDataString += subjectiveSeparator
            subjDataString += compressIntakeConeOrientation
            subjDataString += if (intakeConeOrientation) "TRUE" else "FALSE"


            if (i + 1 != teamNumbers.size) subjDataString += subjectiveTeamSeparator
        }


        // Compress and add all Subjective Match Collection data including previously compressed timeline actions.
        compressedMatchInformation =
            subjectiveStartCharacter + compressedMatchInformation + genericSectionSeparator + subjDataString
    }

    // Remove unnecessary brackets left from type conversion.
    compressedMatchInformation = compressedMatchInformation.replace("[", "")

    Log.d("compression", compressedMatchInformation)

    return compressedMatchInformation
}

fun getRankForTeam(teamRankings: SubjectiveTeamRankings, teamNumber: String): Int {
    return teamRankings.notNullList.first { it.teamNumber == teamNumber }.rank
}

fun subjectiveTeamRankingsToList(teamRankings: SubjectiveTeamRankings): List<String> {
    return teamRankings.notNullList.map { it.teamNumber }
}

