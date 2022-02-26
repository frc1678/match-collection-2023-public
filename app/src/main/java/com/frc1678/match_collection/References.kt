// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.os.CountDownTimer

// File to store information to be used to create the final match information map.
var match_timer: CountDownTimer? = null
var match_time: String = ""
var is_teleop_activated: Boolean = false
var popup_open = false
var is_match_time_ended: Boolean = false
var collection_mode: Constants.ModeSelection = Constants.ModeSelection.NONE
var assign_mode: Constants.AssignmentMode = Constants.AssignmentMode.NONE

// Data that is shared between the objective and subjective QRs.
var serial_number: String? = ""
var match_number: Int = 0
var alliance_color: Constants.AllianceColor = Constants.AllianceColor.NONE
var timestamp: Long = 0
var match_collection_version_number: String = "0.0.1"
var scout_name: String = Constants.NONE_VALUE

// Data specific to Objective Match Collection QR.
var team_number: String = ""
var scout_id: String = Constants.NONE_VALUE
var orientation: Boolean = true //true = UP, false = DOWN
var starting_position: Constants.StartingPosition = Constants.StartingPosition.NONE
var timeline: ArrayList<HashMap<String, String>> = ArrayList()
var climb_level: Constants.ClimbLevel = Constants.ClimbLevel.NONE

var climb_timer: CountDownTimer? = null
var climb_timer_paused = false
var climb_time: Int = 0
var climb_start_time: String? = null
var climb_end_time: String? = null

// Data specific to Subjective Match Collection QR.
var quickness_score: ArrayList<String> = ArrayList()
var field_awareness_score: ArrayList<String> = ArrayList()
var far_field_rating: ArrayList<String> = ArrayList()
var can_shoot_far_list: ArrayList<String> = ArrayList()

// Function to reset References.kt variables for new match.
fun resetCollectionReferences() {
    is_teleop_activated = false

    climb_timer = null
    climb_timer_paused = false
    climb_time = 0
    popup_open = false
    climb_level = Constants.ClimbLevel.NONE

    timestamp = 0

    timeline = ArrayList()

    quickness_score = ArrayList()
    field_awareness_score = ArrayList()
    far_field_rating = ArrayList()
    can_shoot_far_list = ArrayList()
}

fun resetStartingReferences() {
    starting_position = Constants.StartingPosition.NONE
    team_number = ""
}
