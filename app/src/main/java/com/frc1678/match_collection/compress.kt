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

    // Define compression characters for objective separators.
    val objectiveStartCharacter = objectiveData.getValue("_start_character").toString()
    val objectiveSeparator = objectiveData.getValue("_separator").toString()
    // Define compression characters for objective data.
    val compressTeamNumber = objectiveData.getValue("team_number").toString().split(",")[0]
    val compressScoutId = objectiveData.getValue("scout_id").toString().split(",")[0]
    val compressStartingPosition = objectiveData.getValue("start_position").toString().split(",")[0]
    val compressTimeline = objectiveData.getValue("timeline").toString().split(",")[0]
    val compressEndgame = objectiveData.getValue("climb_level").toString().split(",")[0]

    // Define compression characters for subjective separators.
    val subjectiveStartCharacter = subjectiveData.getValue("_start_character").toString()
    val subjectiveSeparator = subjectiveData.getValue("_separator").toString()
    val subjectiveTeamSeparator = subjectiveData.getValue("_team_separator").toString()
    // Define compression characters for subjective data.
    val subjectiveTeamNumberSeparator = subjectiveData.getValue("team_number").toString().split(",")[0]
    val compressQuicknessRankings = subjectiveData.getValue("quickness_score").toString().split(",")[0]
    val compressNearAwareRankings =
        subjectiveData.getValue("near_field_awareness_score").toString().split(",")[0]
    val compressFarAwareRankings = subjectiveData.getValue("far_field_awareness_score").toString().split(",")[0]
    val compressTeamsScoredFar = subjectiveData.getValue("scored_far").toString().split(",")[0]

    // Compress and add data shared between the objective and subjective modes.
    compressedMatchInformation =
        compressSchemaVersion + schemaVersion + genericSeparator +
                compressSerialNumber + serial_number + genericSeparator +
                compressMatchNumber + match_number + genericSeparator +
                compressTimestamp + timestamp + genericSeparator +
                compressVersionNum + match_collection_version_number + genericSeparator +
                compressScoutName + scout_name.toUpperCase(Locale.US)

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
        compressedMatchInformation = objectiveStartCharacter + compressedMatchInformation + genericSectionSeparator +
                compressTeamNumber + team_number + objectiveSeparator +
                compressScoutId + scout_id + objectiveSeparator +
                compressStartingPosition + starting_position.toString() + objectiveSeparator +
                compressTimeline + compressTimelineActions + objectiveSeparator +
                compressEndgame + climb_level
    }
    // Compress and add data specific to Subjective Match Collection.
    else if (collection_mode == Constants.ModeSelection.SUBJECTIVE) {
        var subjDataString = ""
        val teamNumbers = (quickness_rankings + driver_field_awareness_near_rankings + driver_field_awareness_far_rankings + can_shoot_far_list).toSet()

        teamNumbers.forEachIndexed{ i, teamNum ->
            subjDataString += subjectiveTeamNumberSeparator
            subjDataString += teamNum
            val quickness = getRankForTeam(quickness_rankings, teamNum)
            val nearAwareness = getRankForTeam(driver_field_awareness_near_rankings, teamNum)
            val farAwareness = getRankForTeam(driver_field_awareness_far_rankings, teamNum)
            val canShootFar = can_shoot_far_list.contains(teamNum)

            subjDataString += subjectiveSeparator
            subjDataString += compressQuicknessRankings
            subjDataString += quickness.toString()

            subjDataString += subjectiveSeparator
            subjDataString += compressNearAwareRankings
            subjDataString += nearAwareness.toString()

            subjDataString += subjectiveSeparator
            subjDataString += compressFarAwareRankings
            subjDataString += farAwareness.toString()

            subjDataString += subjectiveSeparator
            subjDataString += compressTeamsScoredFar
            subjDataString += if(canShootFar) "TRUE" else "FALSE"

            if(i+1 != teamNumbers.size) subjDataString += subjectiveTeamSeparator
        }


        // Compress and add all Subjective Match Collection data including previously compressed
        // timeline actions.
        compressedMatchInformation = subjectiveStartCharacter + compressedMatchInformation + genericSectionSeparator + subjDataString
    }

    // Remove unnecessary brackets left from type conversion.
    compressedMatchInformation = compressedMatchInformation.replace("[", "")

    Log.d("compression", compressedMatchInformation)

    return compressedMatchInformation
}

fun getRankForTeam(array: List<String>, teamNumber: String): Int {
    return array.mapIndexed { i, s -> RankedTeam(s, i+1) }.first { it.teamNumber == teamNumber }.rank
}

data class RankedTeam(val teamNumber: String, val rank: Int)