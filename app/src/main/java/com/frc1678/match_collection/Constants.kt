// Copyright (c) 2019 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.app.Activity

// Contains constant values and enum classes.
class Constants {
    companion object {
        const val NONE_VALUE: String = "NONE"
        const val NUMBER_OF_ACTIVE_SCOUTS: Int = 18
        const val COMPRESSED_QR_TAG = "QR"
        const val PREVIOUS_SCREEN = "previous_screen"

        /**
         * The previous activity that was visited before this one. This is found by looking for the
         * intent extra with key [PREVIOUS_SCREEN].
         */
        val Activity.previousScreen
            get() = intent.getSerializableExtra(PREVIOUS_SCREEN) as? Screens
    }

    /**
     * Every screen in the app.
     */
    enum class Screens {
        COLLECTION_OBJECTIVE,
        COLLECTION_SUBJECTIVE,
        MATCH_INFORMATION_INPUT,
        MATCH_INFORMATION_EDIT,
        MODE_COLLECTION_SELECT,
        QR_GENERATE,
        STARTING_GAME_PIECE,
        STARTING_POSITION_OBJECTIVE
    }

    enum class ModeSelection {
        SUBJECTIVE,
        OBJECTIVE,
        NONE
    }

    enum class AllianceColor {
        RED,
        BLUE,
        NONE
    }

    enum class StartingPosition {
        `0`,
        `1`,
        `2`,
        `3`,
        `4`,
        NONE
    }

    enum class ChargeLevel {
        N, // NONE
        F, // FAILED
        P, // PARKED
        D, // DOCKED
        E  // ENGAGED
    }

    enum class GamePiecePositions {
        O, // CONE
        U, // CUBE
        N  // NONE
    }

    enum class Preloaded {
        O, // CONE
        U, // CUBE
        N  // NONE
    }

    enum class ActionType {
        SCORE_CUBE_HIGH,
        SCORE_CUBE_MID,
        SCORE_CUBE_LOW,
        SCORE_CONE_HIGH,
        SCORE_CONE_MID,
        SCORE_CONE_LOW,
        SCORE_FAIL,
        INTAKE_STATION,
        INTAKE_LOW_ROW,
        INTAKE_GROUND,
        AUTO_INTAKE_ONE,
        AUTO_INTAKE_TWO,
        AUTO_INTAKE_THREE,
        AUTO_INTAKE_FOUR,
        START_INCAP,
        END_INCAP,
        CHARGE_ATTEMPT,
        TO_TELEOP
    }

    enum class Stage {
        AUTO,
        TELEOP
    }

    enum class AssignmentMode {
        NONE,
        AUTOMATIC_ASSIGNMENT,
        OVERRIDE
    }
}
